package ru.varino.common.exceptions;

public class NotEnoughRightsException extends RuntimeException {


    @Override
    public String getMessage() {
        return "Недостаточно прав на изменение";
    }
}
