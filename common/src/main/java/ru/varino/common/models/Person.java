package ru.varino.common.models;


import ru.varino.common.models.modelUtility.builders.PersonBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Модель человека
 */
public final class Person implements Comparable<Person>, Serializable {
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

    public Person(String name, LocalDateTime birthday, Long weight, Country nationality) {
        this.name = name;
        this.birthday = birthday;
        this.weight = weight;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public Long getWeight() {
        return weight;
    }

    public Country getNationality() {
        return nationality;
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



}