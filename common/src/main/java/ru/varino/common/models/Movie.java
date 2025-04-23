package ru.varino.common.models;

import ru.varino.common.models.modelUtility.builders.MovieBuilder;
import ru.varino.common.utility.Validatable;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Модель фильма
 */
public final class Movie implements Comparable<Movie>, Validatable, Serializable {

    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates; //Поле не может быть null
    private final LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Integer oscarsCount; //Значение поля должно быть больше 0, Поле может быть null
    private final int totalBoxOffice; //Значение поля должно быть больше 0
    private final String tagline; //Поле может быть null
    private final MovieGenre genre; //Поле не может быть null
    private final Person director; //Поле может быть null

    /**
     * Конструктор фильма
     *
     * @param builder {@link MovieBuilder} билдер фильма
     */
    public Movie(MovieBuilder builder) {
        name = builder.getName();
        coordinates = builder.getCoordinates();
        creationDate = builder.getCreationDate();
        oscarsCount = builder.getOscarsCount();
        totalBoxOffice = builder.getTotalBoxOffice();
        tagline = builder.getTagline();
        genre = builder.getGenre();
        director = builder.getDirector();


    }


    public String getName() {
        return name;
    }


    public Coordinates getCoordinates() {
        return coordinates;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }


    public Integer getOscarsCount() {
        return oscarsCount;
    }


    public int getTotalBoxOffice() {
        return totalBoxOffice;
    }


    public String getTagline() {
        return tagline;
    }


    public MovieGenre getGenre() {
        return genre;
    }


    public Person getDirector() {
        return director;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    /**
     * Сравнить два фильма по количеству оскаров и кассовых сборов
     *
     * @param m фильм для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Movie m) {
        int delta;
        if (this.oscarsCount == null || m.getOscarsCount() == null) {
            delta = this.totalBoxOffice - m.getTotalBoxOffice();

        } else {
            delta = (this.oscarsCount + this.totalBoxOffice) - (m.getOscarsCount() + m.getTotalBoxOffice());
        }
        return Integer.compare(delta, 0);
    }


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", oscarsCount=" + oscarsCount +
                ", totalBoxOffice=" + totalBoxOffice +
                ", tagline='" + tagline + '\'' +
                ", genre=" + genre +
                ", director=" + director +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isBlank()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (creationDate == null) return false;
        if (oscarsCount != null && oscarsCount <= 0) return false;
        if (totalBoxOffice <= 0) return false;
        if (genre == null) return false;
        return director == null || director.validate();
    }
}