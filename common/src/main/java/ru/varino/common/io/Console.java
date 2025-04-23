package ru.varino.common.io;

import ru.varino.common.communication.ResponseEntity;

/**
 * Интерфейс консоли для ввода команд и вывода результата
 */
public interface Console {

    /**
     * Напечатать с переводом строки
     *
     * @param text текст для печати
     */
    void println(String text);

    /**
     * Напечатать ошибку
     *
     * @param text текст ошибки
     */
    void printerr(String text);

    /**
     * Напечатать с форматированием
     *
     * @param text текст для печати
     */
    void printf(String text);

    /**
     * Напечатать ответ команды
     *
     * @param response ответ команды
     */
    void printResponse(ResponseEntity response);
}