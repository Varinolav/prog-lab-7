package ru.varino.server.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.models.*;

import ru.varino.server.db.query.MovieQuery;
import ru.varino.server.db.repository.MovieRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieService implements MovieRepository {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class.getSimpleName());
    private Connection connection;

    public MovieService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Movie movie) {
        try (PreparedStatement ps = connection.prepareStatement(MovieQuery.SAVE_PERSON.getSql())) {
            ps.setLong(1, movie.getOwnerId());
            ps.setString(2, movie.getName());
            ps.setDouble(3, movie.getCoordinates().getX().doubleValue());
            ps.setDouble(4, movie.getCoordinates().getY().doubleValue());
            ps.setInt(5, movie.getOscarsCount() == null ? 0 : movie.getOscarsCount());
            ps.setInt(6, movie.getTotalBoxOffice());
            ps.setString(7, movie.getTagline());
            ps.setString(8, movie.getGenre().toString());
            ps.setString(9, movie.getDirector().getName());
            ps.setDate(10, movie.getDirector().getBirthday() == null ? null : Date.valueOf(movie.getDirector().getBirthday().toLocalDate()));
            ps.setLong(11, movie.getDirector().getWeight() == null ? 0 : movie.getDirector().getWeight());
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
        try (PreparedStatement ps = connection.prepareStatement(MovieQuery.REMOVE_BY_ID.getSql())) {
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Фильм с id {} успешно удален", id);
        } catch (SQLException e) {
            logger.error("Произошла ошибка при удалении", e);
        }

    }

    @Override
    public List<Movie> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(MovieQuery.FIND_ALL.getSql())) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Movie> movies = new ArrayList<>();
                while (rs.next()) {
                    optionalBoxing(rs).ifPresent(movies::add);
                }

                return movies;

            }
        } catch (SQLException e) {
            logger.error("Ошибка получения всех фильмов", e);
            return new ArrayList<>();
        }
    }

    private Optional<Movie> optionalBoxing(ResultSet result) throws SQLException {
        if (result == null) return Optional.empty();

        return
                Optional.of(new Movie(
                                result.getInt("id"),
                                result.getInt("owner_id"),
                                result.getString("name"),
                                new Coordinates(
                                        BigDecimal.valueOf(result.getFloat("cord_x")),
                                        BigDecimal.valueOf(result.getFloat("cord_y"))
                                ),
                                result.getTimestamp("creation_date").toLocalDateTime().toLocalDate(),
                                result.getInt("oscars_count"),
                                result.getInt("total_box_office"),
                                result.getString("tagline"),
                                MovieGenre.valueOf(result.getString("genre")),
                                new Person(
                                        result.getString("director_name"),
                                        result.getTimestamp("director_birthday") == null ? null : result.getTimestamp("director_birthday").toLocalDateTime(),
                                        result.getLong("director_weight"),
                                        Country.valueOf(result.getString("director_nationality")))
                        )
                );

    }
}

