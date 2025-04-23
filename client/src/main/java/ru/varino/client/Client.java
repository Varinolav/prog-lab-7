package ru.varino.client;

import ru.varino.client.helpers.UserService;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.exceptions.EmptyFileException;
import ru.varino.common.exceptions.PermissionDeniedException;
import ru.varino.common.exceptions.RecursionException;
import ru.varino.common.io.Console;
import ru.varino.common.models.Movie;
import ru.varino.common.models.modelUtility.InteractiveMovieCreator;
import ru.varino.common.utility.RecursionConfiguration;
import ru.varino.common.utility.RecursionDequeHandler;
import ru.varino.common.utility.ScannerManager;


import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;

import java.net.Socket;
import java.util.Scanner;


public class Client  implements Runnable {
    private InetAddress host;
    private int port;
    private UserService userService;
    private Console console;
    private RecursionDequeHandler recursionDequeHandler;
    private ScannerManager scannerManager;


    public Client(InetAddress host, int port, UserService userService, Console console, RecursionDequeHandler recursionDequeHandler, ScannerManager scannerManager) {
        this.host = host;
        this.port = port;
        this.userService = userService;
        this.console = console;
        this.recursionDequeHandler = recursionDequeHandler;
        this.scannerManager = scannerManager;
    }

    public void run() {
        requestToServer();
    }

    private void requestToServer() {
        boolean status = true;
        while (status) {
            try {
                RequestEntity request = userService.handle();
                if (request.getCommand().isEmpty()) continue;

                if (request.getCommand().equals("execute_script")) {
                    executeScript(request.getParams());
                    continue;
                }
                sendAndReceive(request);
            } catch (Exception e) {
                console.printerr(e.toString());
                status = false;
            }
        }
        console.println("Работа программы завершена");
        System.exit(0);
    }

    private static byte[] serializeRequest(RequestEntity request) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
            return bos.toByteArray();
        }
    }

    private void sendAndReceive(RequestEntity request) {
        try (Socket socket = new Socket(host, port)) {
            OutputStream out = socket.getOutputStream();
            out.write(serializeRequest(request));
            out.flush();

            if (request.getCommand().equals("save")) {
                System.exit(0);
            }
            InputStream input = socket.getInputStream();

            ObjectInputStream ois = new ObjectInputStream(input);

            ResponseEntity response = (ResponseEntity) ois.readObject();
            console.printResponse(response);
        } catch (ClassNotFoundException e) {
            console.printerr("Класса с таким именем не существует");
        } catch (ConnectException e) {
            console.printerr("Сервер временно недоступен");
        } catch (Exception e) {
            console.printerr(e.toString());
        }
    }

    private void executeScript(String fileName) {
        try {
            String[] fileCommand;
            File filePath = new File(fileName);
            if (!filePath.exists()) throw new FileNotFoundException();
            if (!filePath.canRead()) throw new PermissionDeniedException("Чтение");

            Scanner fileScanner = new Scanner(filePath);
            scannerManager.setCurrentScanner(fileScanner);
            if (!fileScanner.hasNext()) throw new EmptyFileException(filePath.toString());

            console.println("Начинается выполнение скрипта из файла %s".formatted(fileName));
            recursionDequeHandler.addFileNameLast(fileName);

            while (fileScanner.hasNextLine()) {
                console.printf("%s -> ~ ".formatted(fileName));
                String scannedCommand = fileScanner.nextLine();

                fileCommand = (scannedCommand.trim() + " ").split(" ", 2);
                String command = fileCommand[0];
                String params = fileCommand[1].trim();
                RequestEntity request = RequestEntity.create(command, params);

                console.println(scannedCommand);
                if (command.equals("execute_script")) {
                    if (recursionDequeHandler.countFileName(params) >= RecursionConfiguration.RECURSION_LIMIT) {
                        throw new RecursionException("Достигнута максимальная глубина рекурсии");
                    }
                    executeScript(fileName);
                    continue;
                }
                if (command.equals("save")) {
                    console.printerr("Команда недоступна на клиенте");
                    request = RequestEntity.create("", "");
                }
                if (command.equals("exit")) {
                    console.println("Завершаем работу программы.");
                    request = RequestEntity.create("save", "");
                }
                if (command.equals("insert") || command.equals("update") || command.contains("replace_if_")) {
                    try {
                        Movie m = InteractiveMovieCreator.create(console, scannerManager.getCurrentScanner());
                        request.body(m);
                    } catch (InterruptedException e) {
                        console.println("Ввод прекращен");
                    }
                }
                sendAndReceive(request);
            }
            recursionDequeHandler.removeFileNameFirst();
        } catch (PermissionDeniedException | RecursionException e) {
            console.printerr(e.getMessage());
        } catch (FileNotFoundException e) {
            console.printerr("Файл не найден.");
        } catch (EmptyFileException e) {
            console.printerr("Файл пуст");
        }
    }
}
