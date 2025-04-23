package ru.varino.server.commands;

import ru.varino.common.models.modelUtility.IdGenerator;
import ru.varino.server.managers.CollectionManager;
import ru.varino.common.models.Movie;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды ReplaceIf
 */
public class ReplaceIf extends Command {
    private final CollectionManager collectionManager;


    private final String type;

    public ReplaceIf(String type, CollectionManager collectionManager) {
        super("replace_if_%s <id>".formatted(type), "заменить значение по ключу, если новое значение " + (type.equals("больше") ? "больше" : "меньше") + " старого");
        this.collectionManager = collectionManager;

        this.type = type;
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
                    .body("Элемента с таким id не существует");
            Movie movie = (Movie) req.getBody();
            Boolean comparisonResult = null;
            if (type.equals("greater")) {
                comparisonResult = movie.compareTo(collectionManager.getElementById(id)) > 0;
            }
            if (type.equals("lower")) {
                comparisonResult = movie.compareTo(collectionManager.getElementById(id)) < 0;
            }
            if (comparisonResult == null) throw new RuntimeException();
            if (comparisonResult) {
                movie.setId(IdGenerator.getInstance().generateId());
                collectionManager.addElementToCollection(id, movie);
                return ResponseEntity.ok().body("Элемент успешно заменен");
            } else {
                return ResponseEntity.badRequest().body("Элемент не заменен");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Ключ должен быть Int");
        }
    }
}
