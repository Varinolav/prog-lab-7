package ru.varino.db.repository;

import ru.varino.common.models.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(int id);

    Optional<User> findByUsername(String username);

    boolean checkPassword(User user, String password);


}
