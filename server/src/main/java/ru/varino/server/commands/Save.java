package ru.varino.server.commands;


import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.server.managers.CollectionManager;

/**
 * Класс команды Save
 */
public class Save extends Command {
    private final CollectionManager collectionManager;

    public Save( CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
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
        return ResponseEntity.ok().body("Коллекция сохранена в файл");
    }
}