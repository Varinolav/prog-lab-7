package ru.varino.db.service;

import ru.varino.common.models.User;
import ru.varino.db.repository.UserRepository;

import java.sql.Connection;
import java.util.Optional;

public class UserService implements UserRepository {
    private Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return false;
    }
}
