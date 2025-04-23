package ru.varino.common.utility;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Интерфейс, описывающий выполнение команды
 */
public interface Executable {
    /**
     * Выполнить команду
     *
     * @param request запрос для выполнения команды
     * @return ответ, содержащий данные о выполнении команды
     */
    ResponseEntity execute(RequestEntity request);
}
