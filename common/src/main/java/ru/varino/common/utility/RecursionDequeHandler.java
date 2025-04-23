package ru.varino.common.utility;


import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Класс обработки рекурсии
 */
public class RecursionDequeHandler {
    private static RecursionDequeHandler instance;
    private final Deque<String> fileNameDeque = new ArrayDeque<>();

    /**
     * Получить инстанс класса
     *
     * @return инстанс класса
     */
    public static RecursionDequeHandler getInstance() {
        return instance == null ? instance = new RecursionDequeHandler() : instance;
    }

    /**
     * Добавить файл в конец деки
     *
     * @param filename имя файла
     */
    public void addFileNameLast(String filename) {
        fileNameDeque.addLast(filename);
    }

    /**
     * Удалить имя файла из начала
     */
    public void removeFileNameFirst() {
        fileNameDeque.removeFirst();
    }

    /**
     * Посчитать количество имен файла в деке
     *
     * @param filename имя файла
     * @return количество имен файла в деке
     */
    public int countFileName(String filename) {
        int count = 0;
        for (String s : fileNameDeque) {
            if (s.equals(filename)) count++;
        }
        return count;
    }

    public boolean isEmpty() {
        return fileNameDeque.isEmpty();
    }
}
