package ru.varino.server.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.varino.server.db.DatabaseConfiguration.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class.getSimpleName());

    private DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("Подключение к базе {}", URL);
            this.connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            logger.info("Подключение установлено");
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Ошибка подключения к базе данных: {}", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static DatabaseManager getInstance() throws SQLException {
        return (instance == null || instance.connection.isClosed()) ? instance = new DatabaseManager() : instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
