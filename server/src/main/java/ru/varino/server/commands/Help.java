package ru.varino.server.commands;

import ru.varino.server.managers.CommandManager;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

import java.util.stream.Collectors;

/**
 * Класс команды Help
 */
public class Help extends Command {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    /**
     * {@inheritDoc}
     * @param req запрос для выполнения команды
     * @return {@link ResponseEntity}
     */
    @Override
    public ResponseEntity execute(RequestEntity req) {
        String args = req.getParams();
        try {
            if (!args.isEmpty())
                return ResponseEntity.ok().body(commandManager.getCommands().get(args).getDescription());

        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Команда не найдена");
        }

        String result = commandManager.getCommands().values().stream()
                .map(command -> command.getName() + " - " + command.getDescription())
                .collect(Collectors.joining("\n"));
        return ResponseEntity.ok().body(result.substring(0, result.length() - 2));
    }
}

