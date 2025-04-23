package ru.varino.common.communication;


import java.io.Serializable;

/**
 * Енам, хранящий статусы ответов
 */
public enum Status implements Serializable {
    OK(200, "OK"),
    BADREQUEST(400, "Bad Request"),
    NOTFOUND(404, "Not found"),
    SERVERERROR(500, "Internal server error");
    final int exitCode;
    final String metaName;

    /**
     * Конструктор енама
     *
     * @param exitCode код завершения
     * @param metaName дополнительная мета информация
     */
    Status(int exitCode, String metaName) {
        this.exitCode = exitCode;
        this.metaName = metaName;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getMetaName() {
        return metaName;
    }
}
