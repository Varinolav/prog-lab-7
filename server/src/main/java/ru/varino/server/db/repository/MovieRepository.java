package ru.varino.server.db.repository;

import ru.varino.common.models.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    void save(Movie movie);

    void removeById(int id);

    List<Movie> findAll();

}
