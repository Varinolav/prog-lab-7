package ru.varino.server.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.models.User;
import ru.varino.server.RunServer;
import ru.varino.server.db.query.UserQuery;
import ru.varino.server.db.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserService implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class.getSimpleName());
    private Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) {
        try (PreparedStatement ps = connection.prepareStatement(UserQuery.SAVE_USER.getSql())) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
            logger.info("Пользователь {} успешно сохранен в базу", user.getUsername());
        } catch (SQLException e) {
            logger.error("Ошибка сохранения пользователя", e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(UserQuery.FIND_BY_ID.getSql())) {
            ps.setInt(1, id);

            try (ResultSet result = ps.executeQuery()) {
                return optionalBoxing(result);
            }
        } catch (SQLException e) {
            logger.error("Пользователь по айди не найден", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(UserQuery.FIND_BY_USERNAME.getSql())) {
            ps.setString(1, username);
            try (ResultSet result = ps.executeQuery()) {
                return optionalBoxing(result);
            }

        } catch (SQLException e) {
            logger.error("Пользователь не найден по имени", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return true;
    }

    @Override
    public Optional<User> findByUsernamePassword(String username, String password) {
        try (PreparedStatement ps = connection.prepareStatement(UserQuery.FIND_BY_USERNAME_PASSWORD.getSql())) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet result = ps.executeQuery()) {
                return optionalBoxing(result);
            }

        } catch (SQLException e) {
            logger.error("Пользователь не найден по имени", e);
            return Optional.empty();
        }
    }

    private Optional<User> optionalBoxing(ResultSet result) throws SQLException {
        if (result == null) return Optional.empty();
        return result.next() ?
                Optional.of(new User(
                        result.getInt("id"),
                        result.getString("username"),
                        result.getString("password")))
                : Optional.empty();
    }
}
