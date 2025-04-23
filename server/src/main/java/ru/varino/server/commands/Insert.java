package ru.varino.server.commands;

import ru.varino.common.models.modelUtility.IdGenerator;
import ru.varino.server.managers.CollectionManager;
import ru.varino.common.models.Movie;
import ru.varino.common.models.modelUtility.InteractiveMovieCreator;
import ru.varino.common.io.Console;
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
        try {
            Integer id = Integer.parseInt(args);
            if (collectionManager.getElementById(id) != null) return ResponseEntity.badRequest()
                    .body("Элемента с таким id уже существует в коллекции");
            Movie movie = (Movie) req.getBody();
            movie.setId(IdGenerator.getInstance().generateId());
            collectionManager.addElementToCollection(id, movie);
            return ResponseEntity.ok().body("Элемент добавлен в коллекцию");

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Ключ должен быть типа Int");
        }
    }
}