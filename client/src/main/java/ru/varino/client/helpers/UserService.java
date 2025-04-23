package ru.varino.client.helpers;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class UserService {
    private Scanner scanner;
    private Console console;
    private RecursionDequeHandler recursionDequeHandler;
    private ScannerManager scannerManager;

    public UserService(Scanner usedScanner, Console console, RecursionDequeHandler recursionDequeHandler, ScannerManager scannerManager) {
        this.scanner = usedScanner;
        this.console = console;
        this.recursionDequeHandler = recursionDequeHandler;
        this.scannerManager = scannerManager;
    }

    public RequestEntity handle() {
        try {
            String[] userCommand;
            console.printf("~ ");
            userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            String command = userCommand[0].toLowerCase();
            String params = userCommand[1].trim();
            RequestEntity request = RequestEntity.create(command, params);
            if (command.equals("save")) {
                console.printerr("Команда недоступна на клиенте");
                return RequestEntity.create("", "");
            }
            if (command.equals("exit")) {
                console.println("Завершаем работу программы.");
                return RequestEntity.create("save", "");
            }
            if (command.equals("insert") || command.equals("update") || command.contains("replace_if_")) {
                try {
                    Movie m = InteractiveMovieCreator.create(console, scanner);
                    request.body(m);
                } catch (InterruptedException e) {
                    console.println("Ввод прекращен");
                }
            }
            return request;
        } catch (NoSuchElementException exception) {
            console.println("");
            console.printerr("Работа программы прекращена!");
            return RequestEntity.create("save", "");
        }

    }
}
