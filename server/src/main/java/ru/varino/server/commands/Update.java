package ru.varino.server.commands;

import ru.varino.common.models.modelUtility.IdGenerator;
import ru.varino.server.managers.CollectionManager;
import ru.varino.common.models.Movie;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды Update
 */
public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update <id>", "обновить значение элемента коллекции, id которого равен заданному");
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
            if (collectionManager.getElementById(id) == null) return ResponseEntity.badRequest()
                    .body("Элемента с таким id не существует в коллекции");
            Movie movie = (Movie) req.getBody();
            movie.setId(IdGenerator.getInstance().generateId());
            collectionManager.addElementToCollection(id, movie);
            return ResponseEntity.ok().body("Элемент успешно перезаписан");

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Ключ должен быть Int");
        }
    }
}
