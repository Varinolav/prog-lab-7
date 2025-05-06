package ru.varino.server.db.repository;

import ru.varino.common.models.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);


    Optional<User> findByUsernamePassword(String username, String password);


}
