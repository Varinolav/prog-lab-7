package ru.varino.server.managers;

import ru.varino.common.models.Movie;

import java.util.*;


/**
 * Класс для работы с коллекцией
 */
public class CollectionManager {
    private Hashtable<Integer, Movie> collection;
    private final Date creationDate;

    public CollectionManager() {
        collection = new Hashtable<Integer, Movie>();
        creationDate = new Date();
    }
    public Hashtable<Integer, Movie> getCollection() {
        return collection;
    }

    public void setCollection(Hashtable<Integer, Movie> collection) {
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
    public static String formatMovies(Hashtable<Integer, Movie> movies) {
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

    public void addElementToCollection(Integer id, Movie movie) {
        collection.put(id, movie);

    }

    public Collection<Movie> getElements() {
        return collection.values();
    }

    public Set<Integer> getElementsIds() {
        return collection.keySet();
    }

    public void removeElementFromCollection(Integer id) {
        collection.remove(id);
    }

    public void clearCollection() {
        collection.clear();
    }

    public Movie getElementById(Integer id) {
        return collection.get(id);
    }
}


