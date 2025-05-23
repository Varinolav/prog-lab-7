package ru.varino.server.managers;

import ru.varino.common.exceptions.NotEnoughRightsException;
import ru.varino.common.exceptions.PermissionDeniedException;
import ru.varino.common.models.Movie;
import ru.varino.common.models.User;
import ru.varino.server.db.DatabaseMap;
import ru.varino.server.db.service.MovieService;
import ru.varino.server.db.service.UserService;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Класс для работы с коллекцией
 */
public class CollectionManager {
    private Map<Integer, Movie> collection;
    private final Date creationDate;
    private final MovieService movieService;
    private final UserService userService;
    private final Lock locker;

    public CollectionManager(MovieService movieService, UserService userService) {
        collection = new DatabaseMap(movieService);
        creationDate = new Date();
        this.locker = new ReentrantLock();
        this.movieService = movieService;
        this.userService = userService;
    }

    public Map<Integer, Movie> getCollection() {
        return collection;
    }

    public void setCollection(Map<Integer, Movie> collection) {
        this.collection = collection;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Получить данны о коллекции
     *
     * @return данные о коллекции
     */
    public String getCollectionInfo() {
        return "Тип данных: " + collection.getClass().getSimpleName() + "\n" +
                "Дата инициализации: " + creationDate + "\n" +
                "Размер коллекции: " + collection.size();
    }

    /**
     * Форматирование вывода
     *
     * @param movies коллекция
     * @return форматированный вывод
     */
    public static String formatMovies(Map<Integer, Movie> movies) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        for (Map.Entry<Integer, Movie> entry : movies.entrySet()) {
            sb.append("  ")
                    .append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append(",\n");
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";
        return formatMovies(collection);
    }

    public void addElementToCollection(Integer id, Movie movie, Object payload) throws NotEnoughRightsException {
        locker.lock();
        try {
            var userId = userService.findByUsername(((User) payload).getUsername()).get().getId();
            if (collection.get(id) != null & movie.getOwnerId() != userId) {
                throw new NotEnoughRightsException();
            }
            movie.setOwnerId(userId);
            collection.put(id, movie);
        } finally {
            locker.unlock();
        }
    }

    public Collection<Movie> getElements() {
        locker.lock();
        try {
            return collection.values();
        } finally {
            locker.unlock();
        }
    }

    public void removeElementFromCollection(Integer id, Object payload) throws NotEnoughRightsException {
        locker.lock();
        try {
            var userId = userService.findByUsername(((User) payload).getUsername()).get().getId();
            Movie movie = collection.get(id);
            if (movie.getOwnerId() != userId) throw new NotEnoughRightsException();
            collection.remove(id);
        } finally {
            locker.unlock();
        }
    }

    public void clearCollection() {
        locker.lock();
        try {
            collection.clear();
        } finally {
            locker.unlock();
        }
    }

    public Movie getElementById(Integer id) {
        locker.lock();
        try {
            return collection.get(id);
        } finally {
            locker.unlock();
        }
    }

    public void load() {
        List<Movie> movieList = movieService.findAll();
        DatabaseMap collectionMap = new DatabaseMap(movieService);
        for (Movie movie : movieList) {
            collectionMap.putWithoutSaving(movie.getId(), movie);
        }
        this.collection = collectionMap;
    }
}


