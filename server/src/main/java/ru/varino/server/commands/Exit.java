package ru.varino.server.commands;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды Exit
 */
public class Exit extends Command {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    /**
     * {@inheritDoc}
     * @param req запрос для выполнения команды
     * @return {@link ResponseEntity}
     */
    @Override
    public ResponseEntity execute(RequestEntity req) {
        return ResponseEntity.exit().body("Работа программы завершена");
    }

}
