package ru.varino.server.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.varino.common.models.Movie;
import ru.varino.common.models.modelUtility.IdGenerator;
import ru.varino.common.io.Console;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import ru.varino.server.managers.utility.adapters.LocalDateAdapter;
import ru.varino.server.managers.utility.adapters.LocalDateTimeAdapter;

/**
 * Класс для парсинга коллекции
 */
public class ParseManager {

    private final Console console;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public ParseManager(Console console) {
        this.console = console;
    }


    /**
     * Конвертирует json в хэш-таблицу
     *
     * @param json json
     * @return хэш-таблица - коллекция
     */
    public Hashtable<Integer, Movie> getHashTableFromJson(String json) {
        IdGenerator idGen = IdGenerator.getInstance();
        try {
            Hashtable<Integer, Movie> movies = new Hashtable<>();
            if (!json.isEmpty()) {
                Type collectionType = new TypeToken<Hashtable<Integer, Movie>>() {
                }.getType();
                movies = gson.fromJson(json, collectionType);
            }
            ArrayList<Integer> IdList = new ArrayList<>();

            Iterator<Map.Entry<Integer, Movie>> iterator = movies.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Movie> entry = iterator.next();
                Movie m = entry.getValue();
                Integer movieId = m.getId();
                if (IdList.contains(movieId) || !m.validate()) {
                    iterator.remove();
                } else {
                    IdList.add(movieId);
                }
            }
            idGen.setIdsFromCollection(movies);
            return movies;
        } catch (Exception e) {
            console.println("Json-файл повреждён, данные из него не были взяты. Коллекция, с которой вы работаете пуста. %s".formatted(e.toString()));
            return new Hashtable<>();
        }
    }

    /**
     * Конвертирует хэш-таблицу в json
     *
     * @param movies коллекция
     * @return json
     */
    public String getJsonFromHashTable(Hashtable<Integer, Movie> movies) {
        try {
            return gson.toJson(movies);
        } catch (Exception e) {
            console.println(e.toString());
            return "Ошибка парсинга";
        }
    }
}