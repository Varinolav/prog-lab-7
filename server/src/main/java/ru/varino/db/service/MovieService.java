package ru.varino.db.service;

import ru.varino.common.models.Movie;
import ru.varino.db.repository.MovieRepository;

import java.sql.Connection;
import java.util.Optional;

public class MovieService  implements MovieRepository {
    private Connection connection;

    public MovieService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Movie movie) {

    }

    @Override
    public Optional<Movie> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void removeById(int id) {

    }
}
