package ru.varino.server.commands;

import ru.varino.common.exceptions.NotEnoughRightsException;
import ru.varino.server.managers.CollectionManager;
import ru.varino.common.models.Movie;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды Insert
 */
public class Insert extends Command {
    private final CollectionManager collectionManager;


    public Insert(CollectionManager collectionManager) {
        super("insert <id>", "добавить новый элемент с заданным ключом");
        this.collectionManager = collectionManager;
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
            if (collectionManager.getElementById(id) != null) return ResponseEntity.badRequest()
                    .body("Элемента с таким id уже существует в коллекции");
            Movie movie = (Movie) req.getBody();
            try {
                collectionManager.addElementToCollection(id, movie, req.getPayload());
            } catch (NotEnoughRightsException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }


            return ResponseEntity.ok().body("Элемент добавлен в коллекцию");

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Ключ должен быть типа Int");
        }
    }
}