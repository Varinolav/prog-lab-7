package ru.varino.common.models.modelUtility.builders;


/**
 * Интерфейс для описания билдера
 *
 * @param <T> класс, который будет билдиться
 */
public interface Builder<T> {

    /**
     * Получить построенный класс
     *
     * @return построенный класс
     */
    T build();

    /**
     * Сбросить билдер
     */
    void reset();
}
