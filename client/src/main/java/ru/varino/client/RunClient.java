package ru.varino.client;

import ru.varino.client.helpers.UserService;
import ru.varino.common.io.Console;
import ru.varino.common.io.StandartConsole;
import ru.varino.common.utility.RecursionDequeHandler;
import ru.varino.common.utility.ScannerManager;
import ru.varino.common.utility.ServerConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Console console = new StandartConsole();
        RecursionDequeHandler recursionDequeHandler = new RecursionDequeHandler();
        ScannerManager scannerManager = new ScannerManager();
        UserService userService = new UserService(scanner, console, recursionDequeHandler, scannerManager);
        try {
            Client client = new Client(InetAddress.getByName(ServerConfiguration.HOST), ServerConfiguration.PORT, userService, console, recursionDequeHandler, scannerManager);
            client.run();
        } catch (UnknownHostException e) {
            System.out.println("Сервера с таким именем не найдено");
        }
    }
}
