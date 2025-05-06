package ru.varino.server.commands;

import ru.varino.common.models.Movie;
import ru.varino.server.db.service.UserService;
import ru.varino.server.managers.CollectionManager;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import ru.varino.common.models.User;

/**
 * Класс команды RemoveLowerKey
 */
public class RemoveLowerKey extends Command {
    private final CollectionManager collectionManager;
    private final UserService userService;

    public RemoveLowerKey(CollectionManager collectionManager, UserService userService) {
        super("remove_lower_key <id>", "удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        this.collectionManager = collectionManager;
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     *
     * @param req запрос для выполнения команды
     * @return {@link ResponseEntity}
     */
    @Override
    public ResponseEntity execute(RequestEntity req) {
        String args = req.getParams();
        if (args.isEmpty()) return ResponseEntity.badRequest().body("Неверные аргументы");
        try {
            Integer id = Integer.parseInt(args);
            for (var entry : collectionManager.getCollection().entrySet()) {
                Integer keyId = entry.getKey();
                Movie movie = entry.getValue();
                String ownerUsername = userService.findById(movie.getOwnerId()).get().getUsername();
                if (ownerUsername.equals(((User) req.getPayload()).getUsername()) & keyId < id) {
                    collectionManager.removeElementFromCollection(keyId, req.getPayload());
                }
            }

            return ResponseEntity.ok().body("Элементы удалены из коллекции");

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Ключ должен быть типа Int");

        }
    }
}
