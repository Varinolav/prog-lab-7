package ru.varino.db.repository;

import ru.varino.common.models.Movie;

import java.util.Optional;

public interface MovieRepository {
    void save(Movie movie);

    Optional<Movie> findById(int id);

    void removeById(int id);

}
