package ru.varino.server.commands;

import ru.varino.server.managers.CollectionManager;
import ru.varino.common.models.Movie;
import ru.varino.common.models.MovieGenre;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды CountLessGenre
 */
public class CountLessGenre extends Command {
    private final CollectionManager collectionManager;

    public CountLessGenre(CollectionManager collectionManager) {
        super("count_less_than_genre <genre>", "вывести количество элементов, значение поля genre которых меньше заданного");
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
        if (collectionManager.getCollection().isEmpty()) return ResponseEntity.badRequest().body("Коллекция пуста");
        try {
            long count = collectionManager.getElements().stream()
                    .filter(e -> e.getGenre()
                            .compareTo(MovieGenre.valueOf(args)) < 0)
                    .count();
            return ResponseEntity.ok().body(String.valueOf(count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверное значение. (Возможные варианты - %s)".formatted(MovieGenre.getNames()));

        }
    }
}
