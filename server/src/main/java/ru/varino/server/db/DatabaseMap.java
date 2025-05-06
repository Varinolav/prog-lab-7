package ru.varino.server.db;


import ru.varino.common.models.Movie;
import ru.varino.server.db.service.MovieService;

import java.util.HashMap;

public class DatabaseMap extends HashMap<Integer, Movie> {
    private MovieService movieService;

    public DatabaseMap(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public Movie put(Integer id, Movie m) {
        movieService.save(m);
        return super.put(id, m);
    }

    @Override
    public Movie remove(Object id) {
        Movie m = super.get(id);
        var movieId = m.getId();
        movieService.removeById(movieId);
        return super.remove(id);
    }

    public void putWithoutSaving(Integer id, Movie m) {
        super.put(id, m);
    }
}