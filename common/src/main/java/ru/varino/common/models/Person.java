package ru.varino.common.models;


import ru.varino.common.models.modelUtility.builders.PersonBuilder;
import ru.varino.common.utility.Validatable;

import java.io.Serializable;

/**
 * Модель человека
 */
public final class Person implements Comparable<Person>, Validatable, Serializable {
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final java.time.LocalDateTime birthday; //Поле может быть null
    private final Long weight; //Поле может быть null, Значение поля должно быть больше 0
    private final Country nationality; //Поле не может быть null

    /**
     * Конструктор человека
     *
     * @param builder {@link PersonBuilder} билдер человека
     */
    public Person(PersonBuilder builder) {
        this.name = builder.getName();
        this.birthday = builder.getBirthday();
        this.weight = builder.getWeight();
        this.nationality = builder.getNationality();
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", weight=" + weight +
                ", nationality=" + nationality +
                '}';
    }

    /**
     * Сравнить два человека по длине их имени
     *
     * @param p человек для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Person p) {
        int delta = name.length() - p.getName().length();
        return Integer.compare(delta, 0);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        if (name == null || name.isBlank()) return false;
        if (weight != null && weight <= 0) return false;
        return nationality != null;

    }
}