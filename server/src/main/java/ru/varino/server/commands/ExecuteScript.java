package ru.varino.server.commands;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды ExecuteScript
 */
public class ExecuteScript extends Command {

    public ExecuteScript() {
        super("execute_script <file_name>", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");

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

        return ResponseEntity.ok().body("Скрипт выполнен");
    }
}
