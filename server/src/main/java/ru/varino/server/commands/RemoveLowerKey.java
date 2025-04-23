package ru.varino.server.commands;

import ru.varino.server.managers.CollectionManager;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс команды RemoveLowerKey
 */
public class RemoveLowerKey extends Command {
    private final CollectionManager collectionManager;

    public RemoveLowerKey(CollectionManager collectionManager) {
        super("remove_lower_key <id>", "удалить из коллекции все элементы, ключ которых меньше, чем заданный");
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
            List<Integer> idsToRemove = collectionManager.getElementsIds().stream()
                    .filter(k -> k < id)
                    .toList();

            idsToRemove.forEach(collectionManager::removeElementFromCollection);
            return ResponseEntity.ok().body("Элементы удалены из коллекции");

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Ключ должен быть типа Int");

        }
    }
}
