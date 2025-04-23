package ru.varino.server.commands;


import ru.varino.common.utility.Executable;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Абстрактный класс команды, описывающий команду
 */
public abstract class Command implements Executable {
    private final String name;
    private final String description;



    protected Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     * @param request запрос для выполнения команды
     * @return ответ команды
     */
    public abstract ResponseEntity execute(RequestEntity request);

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
