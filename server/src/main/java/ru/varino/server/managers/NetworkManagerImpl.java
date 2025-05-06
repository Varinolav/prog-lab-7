package ru.varino.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.models.User;
import ru.varino.common.utility.*;
import ru.varino.server.db.DatabaseManager;
import ru.varino.server.db.service.MovieService;
import ru.varino.server.db.service.UserService;
import ru.varino.server.managers.utility.ClientData;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManagerImpl implements NetworkManager {
    private InetSocketAddress address;
    private RequestManager requestManager;


    private final Map<SocketChannel, ClientData> clientData;

    private Selector selector;

    private ExecutorService cachedPool;
    private ExecutorService fixedPool;

    private static final Logger logger
            = LoggerFactory.getLogger(NetworkManagerImpl.class.getSimpleName());

    public NetworkManagerImpl(InetSocketAddress address, RequestManager requestManager) {
        this.address = address;
        this.requestManager = requestManager;
        this.cachedPool = Executors.newCachedThreadPool();
        this.fixedPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.clientData = new ConcurrentHashMap<>();
    }

    @Override
    public void connect() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(address);
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            this.selector = selector;
            logger.info("Сервер работает на хосте {}, порт {}", address.getHostName(), address.getPort());
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода");
        }
    }

    @Override
    public void startHandling() {
        while (true) {
            try {
                if (selector.selectNow() > 0) {
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();

                        if (!key.isValid()) {
                            System.out.println("invalid"); // log
                        }
                        if (key.isAcceptable()) {
                            acceptKey(key);
                        } else if (key.isReadable()) {
                            cachedPool.submit(() -> {
                                try {
                                    readClientData(key);
                                } catch (ClassNotFoundException e) {
                                    logger.error("Класс не найден");

                                } catch (IOException e) {
                                    logger.error("Ошибка ввода-вывода");
                                }
                            });
                            fixedPool.submit(() -> processRequest(key));
                            Thread sending = new Thread(() -> sendData(key));
                            sending.start();
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Ошибка ввода-вывода");
        }
    }}

    private void acceptKey(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);

            clientChannel.register(selector, SelectionKey.OP_READ);
            clientData.put(clientChannel, new ClientData());
            logger.info("Принято новое соединение: {}", clientChannel.getRemoteAddress());
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода");
            closeConnection(key);
        }

    }

    private void readClientData(SelectionKey key) throws ClassNotFoundException, IOException {
        try {
            SocketChannel userChannel = (SocketChannel) key.channel();
            userChannel.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int bytesRead = userChannel.read(buffer);
            buffer.flip();

            ByteArrayInputStream is = new ByteArrayInputStream(buffer.array(), 0, bytesRead);
            ObjectInputStream ois = new ObjectInputStream(is);
            RequestEntity request = (RequestEntity) ois.readObject();

            clientData.put(userChannel, clientData.get(userChannel).setRequestEntity(request));
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода");
        }
    }


    private void processRequest(SelectionKey key) {
        try {
            SocketChannel userChannel = (SocketChannel) key.channel();
            RequestEntity request = clientData.get(userChannel).requestEntity();

            ResponseEntity response = requestManager.process(request);
            logger.info("Команда {} с параметрами '{}' и объектом {} успешно обработана. Ее прислал пользователь {}", request.getCommand(), request.getParams(), request.getBody(), ((User) request.getPayload()).getUsername());
            userChannel.register(selector, SelectionKey.OP_WRITE);
            clientData.put(userChannel, clientData.get(userChannel).setResponseEntity(response));
        } catch (Exception e) {
            logger.error("Ошибка", e);
        }
    }

    private void sendData(SelectionKey key) {
        SocketChannel userChannel = (SocketChannel) key.channel();
        try {
            userChannel.configureBlocking(false);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            ResponseEntity response = clientData.get(userChannel).responseEntity();
            oos.writeObject(response);
            ByteBuffer buffer = ByteBuffer.wrap(os.toByteArray());
            userChannel.write(buffer);
            logger.info("Ответ отправлен на клиент");
            userChannel.close();
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода");
            closeConnection(key);
        }
    }

    private void closeConnection(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();
            key.cancel();
            client.close();
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода");

        }
    }
}
