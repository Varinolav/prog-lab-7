package ru.varino.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.utility.*;
import ru.varino.db.DatabaseManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NetworkManagerImpl implements NetworkManager {
    private InetSocketAddress address;
    private RequestManager requestManager;
    private DatabaseManager databaseManager;

    private Selector selector;

    private static final Logger logger
            = LoggerFactory.getLogger(NetworkManagerImpl.class.getSimpleName());

    public NetworkManagerImpl(InetSocketAddress address, RequestManager requestManager, DatabaseManager databaseManager) {
        this.address = address;
        this.requestManager = requestManager;
        this.databaseManager = databaseManager;
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
                            ResponseEntity response = processKey(key);
                            if (response != null) {
                                sendData(key, response);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Ошибка ввода-вывода");
            }
        }
    }

    private void acceptKey(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);

            clientChannel.register(selector, SelectionKey.OP_READ);
            logger.info("Принято новое соединение: {}", clientChannel.getRemoteAddress());
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода");
            closeConnection(key);
        }

    }

    private ResponseEntity processKey(SelectionKey key) {
        try {
            SocketChannel userChannel = (SocketChannel) key.channel();
            userChannel.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int bytesRead = userChannel.read(buffer);
            buffer.flip();

            ByteArrayInputStream is = new ByteArrayInputStream(buffer.array(), 0, bytesRead);
            ObjectInputStream ois = new ObjectInputStream(is);
            ResponseEntity response;
            try {
                RequestEntity request = (RequestEntity) ois.readObject();
                response = requestManager.process(request);
                logger.info("Команда {} с параметрами '{}' и объектом {} успешно обработана", request.getCommand(), request.getParams(), request.getBody());
            } catch (ClassNotFoundException e) {
                response = ResponseEntity.badRequest().body("Такого класса не существует");
                logger.error("Класс не найден");
            }
            userChannel.register(selector, SelectionKey.OP_WRITE);
            return response;
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода");

            closeConnection(key);
            return ResponseEntity.badRequest();
        }
    }

    private void sendData(SelectionKey key, ResponseEntity response) {
        SocketChannel userChannel = (SocketChannel) key.channel();
        try {
            userChannel.configureBlocking(false);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
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
