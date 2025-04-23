package ru.varino.common.models.modelUtility;

import ru.varino.common.models.Movie;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Класс для генерации айди элементов коллекции
 */
public class IdGenerator {
    private static IdGenerator instance;
    private Integer ID = 1;
    private HashSet<Integer> idList = new HashSet<>();

    public static IdGenerator getInstance() {
        return instance == null ? instance = new IdGenerator() : instance;
    }

    /**
     * Установить все существующие айдишники в коллекции
     *
     * @param collection коллекция
     */
    public void setIdsFromCollection(Hashtable<Integer, Movie> collection) {
        HashSet<Integer> ids = new HashSet<>();
        for (Movie m : collection.values()) {
            Integer id = m.getId();
            ids.add(id);
        }
        idList = ids;
    }

    /**
     * Сгенерировать новый айди
     *
     * @return айди
     */
    public Integer generateId() {
        while (idList.contains(ID)) {
            ID++;
        }
        idList.add(ID);
        return ID;
    }

}
