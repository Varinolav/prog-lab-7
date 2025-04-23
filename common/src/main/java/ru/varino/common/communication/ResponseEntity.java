package ru.varino.common.communication;

import java.io.Serializable;

/**
 * Класс ответа команды
 */
public class ResponseEntity implements Serializable {
    private final int exitCode;
    private final String metaName;
    private String body;

    /**
     * Конструктор ответа
     *
     * @param status {@link Status} Енам статуса
     */
    private ResponseEntity(Status status) {
        this.exitCode = status.getExitCode();
        this.metaName = status.getMetaName();
    }

    /**
     * Добавить тело ответа
     *
     * @param body тело ответа
     * @return этот же ответ
     */
    public ResponseEntity body(String body) {
        this.body = body;
        return this;
    }

    public String getBody() {
        return body;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getMetaName() {
        return metaName;
    }

    /**
     * Вернуть ответ со статусом "Ок"
     *
     * @return ответ "Ок"
     */
    public static ResponseEntity ok() {
        return new ResponseEntity(Status.OK);
    }

    /**
     * Вернуть ответ со статусом "Плохой запрос"
     *
     * @return ответ "Плохой запрос"
     */
    public static ResponseEntity badRequest() {
        return new ResponseEntity(Status.BADREQUEST);
    }

    /**
     * Вернуть ответ со статусом "Не найдено"
     *
     * @return ответ "Не найдено"
     */
    public static ResponseEntity notFound() {
        return new ResponseEntity(Status.NOTFOUND);
    }

    /**
     * Вернуть ответ со статусом "Ошибка сервера"
     *
     * @return ответ "Ошибка сервера"
     */
    public static ResponseEntity serverError() {
        return new ResponseEntity(Status.SERVERERROR);
    }

    /**
     * Вернуть ответ со статусом "Выход"
     *
     * @return ответ "Выход"
     */
    public static ResponseEntity exit() {
        return new ResponseEntity(Status.SERVERERROR);
    }
}
