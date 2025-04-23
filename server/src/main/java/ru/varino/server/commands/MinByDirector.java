package ru.varino.server.commands;

import ru.varino.server.managers.CollectionManager;
import ru.varino.common.models.Movie;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

import java.util.Comparator;

/**
 * Класс команды MinByDirector
 */
public class MinByDirector extends Command {
    private final CollectionManager collectionManager;

    public MinByDirector(CollectionManager collectionManager) {
        super("min_by_director", "вывести любой объект из коллекции, значение поля director которого является минимальным");
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
        if (!args.isEmpty()) return ResponseEntity.badRequest().body("Неверные аргументы");
        if (collectionManager.getCollection().isEmpty()) return ResponseEntity.badRequest().body("Коллекция пуста");


        Movie minMovieByDirector = collectionManager.getElements().stream()
                .filter(m -> m.getDirector() != null)
                .min(Comparator.comparing(Movie::getDirector))
                .orElse(null);

        if (minMovieByDirector == null)
            return ResponseEntity.badRequest().body("У всех элементов поле director не определено");

        return ResponseEntity.ok().body(minMovieByDirector.toString());
    }
}
