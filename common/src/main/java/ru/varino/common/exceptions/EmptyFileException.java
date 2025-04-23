package ru.varino.common.exceptions;

/**
 * Исключение пустого файла
 */
public class EmptyFileException extends Exception {
    public EmptyFileException(String fileName) {
        super(fileName);
    }

    @Override
    public String getMessage() {
        return "Файл %s пуст".formatted(super.getMessage());
    }
}
