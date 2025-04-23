package ru.varino.common.models;


import java.io.Serializable;

/**
 * Енам жанров
 */
public enum MovieGenre implements Serializable {
    WESTERN,
    COMEDY,
    ADVENTURE,
    THRILLER,
    HORROR;

    /**
     * Получить все имена енамов
     *
     * @return строка с перечислением
     */
    public static String getNames() {
        StringBuilder genreBuilder = new StringBuilder();
        for (MovieGenre genre : values()) {
            genreBuilder.append(genre).append(", ");
        }
        return genreBuilder.substring(0, genreBuilder.length() - 2);
    }
}