package ru.varino.server.managers;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.server.commands.Command;


public class RequestManager {
    private CommandManager commandManager;

    public RequestManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public ResponseEntity process(RequestEntity req) {
        String commandReq = req.getCommand();
        if (commandReq.isEmpty()) return ResponseEntity.badRequest().body("Введено 0 аргументов");
        Command command = commandManager.getCommand(commandReq);
        if (command == null) {
            return ResponseEntity.badRequest().body("Команда '" + commandReq + "' не найдена, воспользуйтесь help");
        }

        return command.execute(req);
    }
}
