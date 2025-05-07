package ru.varino.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.utility.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManagerImpl implements NetworkManager {
    private InetSocketAddress address;
    private RequestManager requestManager;

    private ExecutorService readPool;
    private ExecutorService writePool;

    private Selector selector;

    private static final Logger logger
            = LoggerFactory.getLogger(NetworkManagerImpl.class.getSimpleName());

    public NetworkManagerImpl(InetSocketAddress address, RequestManager requestManager) {
        this.address = address;
        this.requestManager = requestManager;
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
            this.readPool = Executors.newCachedThreadPool();
            this.writePool = Executors.newFixedThreadPool(10);
            logger.info("Сервер работает на хосте {}, порт {}", address.getHostName(), address.getPort());
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода, {}", e.getStackTrace());
            System.out.println(e.getMessage());


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
                            logger.warn("Невалидный ключ");
                        }
                        if (key.isAcceptable()) {
                            acceptKey(key);
                        } else if (key.isReadable()) {
                            key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
                            processKey(key);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Ошибка ввода-вывода, {}", e.getStackTrace());
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
            logger.error("Ошибка ввода-вывода, {}", e.getStackTrace());
            closeConnection(key);
        }
    }

    private void processKey(SelectionKey key) {
        readPool.submit(() -> {
            try {
                SocketChannel userChannel = (SocketChannel) key.channel();
                userChannel.configureBlocking(false);
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                int bytesRead = userChannel.read(buffer);
                buffer.flip();
                ByteArrayInputStream is = new ByteArrayInputStream(buffer.array(), 0, bytesRead);
                ObjectInputStream ois = new ObjectInputStream(is);
                RequestEntity request = (RequestEntity) ois.readObject();

                processAndSend(key, request);
            } catch (IOException e) {
                logger.error("Ошибка ввода-вывода, {}", e.getStackTrace());
                closeConnection(key);
            } catch (ClassNotFoundException e) {
                logger.error("Класс не найден");
            }
        });
    }

    private void processAndSend(SelectionKey key, RequestEntity request) {
        writePool.submit(() -> {
            ResponseEntity response = requestManager.process(request);
            sendData(key, response);
        });
    }



    private void sendData(SelectionKey key, ResponseEntity response) {
        new Thread(() -> {
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
                logger.error("Ошибка ввода-вывода, {}", e.getStackTrace());
                closeConnection(key);
            }
        }).start();

    }

    private void closeConnection(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();
            key.cancel();
            client.close();
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода, {}", e.getStackTrace());
        }
    }
}
