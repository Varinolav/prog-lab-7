package ru.varino.server.commands;


import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.exceptions.PermissionDeniedException;
import ru.varino.common.models.Movie;
import ru.varino.server.managers.CollectionManager;
import ru.varino.server.managers.FileManager;
import ru.varino.server.managers.ParseManager;

import java.util.Hashtable;

/**
 * Класс команды Save
 */
public class Save extends Command {
    private final ParseManager parseManager;
    private final FileManager fileManager;
    private final CollectionManager collectionManager;

    public Save(ParseManager parseManager, FileManager fileManager, CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.parseManager = parseManager;
        this.fileManager = fileManager;
        this.collectionManager = collectionManager;
    }

    /**
     * {@inheritDoc}
     * @param req запрос для выполнения команды
     * @return {@link ResponseEntity}
     */
    @Override
    public ResponseEntity execute(RequestEntity req) {
        String args = req.getParams();
        if (!args.isEmpty()) return ResponseEntity.badRequest().body("Неверные аргументы");
        try {
            Hashtable<Integer, Movie> collection = collectionManager.getCollection();
            String json = parseManager.getJsonFromHashTable(collection);
            fileManager.write(json);
        } catch (PermissionDeniedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
        return ResponseEntity.ok().body("Коллекция сохранена в файл");
    }
}