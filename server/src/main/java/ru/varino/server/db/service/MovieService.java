package ru.varino.server.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.models.Movie;

import ru.varino.server.db.query.MovieQuery;
import ru.varino.server.db.repository.MovieRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieService implements MovieRepository {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class.getSimpleName());
    private Connection connection;

    public MovieService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Movie movie) {
        try (PreparedStatement ps = connection.prepareStatement(MovieQuery.SAVE_PERSON.getSql())) {
            ps.setInt(1, movie.getOwnerId());
            ps.setString(2, movie.getName());
            ps.setDouble(3, movie.getCoordinates().getX().doubleValue());
            ps.setDouble(4, movie.getCoordinates().getY().doubleValue());
            ps.setInt(5, movie.getOscarsCount());
            ps.setInt(6, movie.getTotalBoxOffice());
            ps.setString(7, movie.getTagline());
            ps.setString(8, movie.getGenre().toString());
            ps.setString(9, movie.getDirector().getName());
            ps.setDate(10, Date.valueOf(movie.getDirector().getBirthday().toLocalDate()));
            ps.setLong(11, movie.getDirector().getWeight());
            ps.setString(12, movie.getDirector().getNationality().toString());


            try (ResultSet generatedId = ps.executeQuery()) {
                if (generatedId.next()) movie.setId(generatedId.getInt(1));
                else throw new SQLException("Произошла ошибка присвоения Id");
            }
            logger.info("Фильм сохранен {}", movie);
        } catch (SQLException e) {
            logger.error("Ошибка сохранения фильма в базу", e);
        }

    }


    @Override
    public void removeById(int id) {

    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>();
    }
}
