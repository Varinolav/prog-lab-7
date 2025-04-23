package ru.varino.common.communication;

import java.io.Serializable;

/**
 * Класс запроса
 */
public class RequestEntity implements Serializable {
    private final String command;
    private final String params;
    private Object body;


    private RequestEntity(String command, String params) {
        this.command = command;
        this.params = params;
    }

    /**
     * добавить тело запроса
     *
     * @param data тело запроса
     * @return этот же запрос
     */
    public RequestEntity body(Object data) {
        this.body = data;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public String getParams() {
        return params;
    }

    /**
     * Создать запрос
     *
     * @param command команда
     * @param params  аргументы команды
     * @return запрос
     */
    public static RequestEntity create(String command, String params) {
        return new RequestEntity(command, params);
    }

    public Object getBody() {
        return body;
    }
}
