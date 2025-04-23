package ru.varino.common.models.modelUtility.builders;

import ru.varino.common.models.Coordinates;
import ru.varino.common.models.Movie;
import ru.varino.common.models.MovieGenre;
import ru.varino.common.models.Person;
import ru.varino.common.models.modelUtility.IdGenerator;

import java.time.LocalDate;

/**
 * Билдер фильмов
 */
public class MovieBuilder implements Builder<Movie> {
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private java.time.LocalDate creationDate;
    private Integer oscarsCount;
    private int totalBoxOffice;
    private String tagline;
    private MovieGenre genre;
    private Person director;

    public MovieBuilder buildId() {
        IdGenerator idGen = IdGenerator.getInstance();
        this.id = idGen.generateId();
        return this;
    }

    public MovieBuilder buildName(String name) {
        if (name == null) throw new IllegalArgumentException("Имя не может быть null");
        if (name.isEmpty()) throw new IllegalArgumentException("имя не может быть пустым");
        this.name = name;
        return this;
    }

    public MovieBuilder buildCoordinates(Coordinates coords) {
        if (coords == null) throw new IllegalArgumentException("Координаты не могут быть null");

        this.coordinates = coords;
        return this;
    }

    public MovieBuilder buildCreationDate(LocalDate creationDate) {
        if (creationDate == null) throw new IllegalArgumentException("Дата создания не может быть null");

        this.creationDate = creationDate;
        return this;

    }

    public MovieBuilder buildOscarsCount(Integer oscarsCount) {
        if (oscarsCount != null && oscarsCount <= 0)
            throw new IllegalArgumentException("Количество оскаров должно быть больше 0");
        this.oscarsCount = oscarsCount;
        return this;

    }

    public MovieBuilder buildTotalBoxOffice(Integer totalBoxOffice) {
        if (totalBoxOffice == null) throw new IllegalArgumentException("Число кассовых сборов не может быть null");
        if (totalBoxOffice <= 0)
            throw new IllegalArgumentException("Число кассовых сборов должно быть больше 0");

        this.totalBoxOffice = totalBoxOffice;
        return this;

    }

    public MovieBuilder buildTagline(String tagline) {
        this.tagline = tagline;
        return this;

    }

    public MovieBuilder buildGenre(MovieGenre genre) {

        if (genre == null) throw new IllegalArgumentException("Жанр не может быть null");

        this.genre = genre;
        return this;

    }

    public MovieBuilder buildDirector(Person director) {
        this.director = director;
        return this;

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

    public Integer getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        name = null;
        coordinates = null;
        creationDate = null;
        oscarsCount = null;
        totalBoxOffice = 0;
        tagline = null;
        genre = null;
        director = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie build() {
        return new Movie(this);
    }
}
