package ru.varino.server.managers;

import ru.varino.server.commands.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы с командами
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * Добавить команду
     *
     * @param name    имя команды
     * @param command {@link Command} класс команды
     * @return {@link CommandManager}
     */
    public CommandManager add(String name, Command command) {
        commands.put(name, command);
        return this;

    }

    /**
     * Получить команду
     *
     * @param name имя команды
     * @return {@link Command} команда
     */
    public Command getCommand(String name) {
        return commands.get(name);
    }





}
