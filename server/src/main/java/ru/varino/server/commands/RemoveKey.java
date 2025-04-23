package ru.varino.server.commands;

import ru.varino.server.managers.CollectionManager;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды RemoveKey
 */
public class RemoveKey extends Command {
    private final CollectionManager collectionManager;

    public RemoveKey(CollectionManager collectionManager) {
        super("remove_key <id>", "удалить элемент из коллекции по его ключу");
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
        if (args.isEmpty()) return ResponseEntity.badRequest().body("Неверные аргументы");

        try {
            Integer id = Integer.parseInt(args);
            if (collectionManager.getElementById(id) == null) return ResponseEntity.badRequest()
                    .body("Элемента с таким id не существует в коллекции");
            collectionManager.removeElementFromCollection(id);
            return ResponseEntity.ok().body("Элемент удален из коллекции");

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Ключ должен быть Int");

        }
    }
}
