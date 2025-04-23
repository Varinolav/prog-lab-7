package ru.varino.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.exceptions.PermissionDeniedException;
import ru.varino.common.io.Console;
import ru.varino.server.RunServer;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandListener {
    private FileManager fileManager;
    private CollectionManager collectionManager;
    private ParseManager parseManager;
    private Console console;
    private static final Logger logger = LoggerFactory.getLogger(CommandListener.class.getSimpleName());

    public CommandListener(FileManager fileManager, CollectionManager collectionManager, ParseManager parseManager, Console console) {
        this.fileManager = fileManager;
        this.collectionManager = collectionManager;
        this.parseManager = parseManager;
        this.console = console;
    }

    public Thread getListener() {
        return new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    String stringInput = scanner.nextLine();
                    if (stringInput.trim().equals("save")) {
                        try {
                            fileManager.write(parseManager.getJsonFromHashTable(collectionManager.getCollection()));
                            console.println("Коллекция сохранена в файл");
                        } catch (PermissionDeniedException e) {
                            logger.error(e.getMessage());
                        }
                    } else if (stringInput.trim().equals("exit")) {
                        console.println("Завершение работы сервера...");
                        System.exit(0);
                    } else {
                        console.println("Доступны только 2 команды: save и exit");
                    }
                } catch (NoSuchElementException exception) {
                    console.println("");
                    logger.error("Остановка программы");
                }
            }
        });

    }
}
