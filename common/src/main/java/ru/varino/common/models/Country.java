package ru.varino.common.models;

import java.io.Serializable;

/**
 * Енам стран
 */
public enum Country implements Serializable {
    UNITED_KINGDOM,
    GERMANY,
    SPAIN,
    THAILAND,
    NORTH_KOREA;

    /**
     * Получить все имена енамов
     *
     * @return строка с перечислением
     */
    public static String getNames() {
        StringBuilder countyBuilder = new StringBuilder();
        for (Country country : values()) {
            countyBuilder.append(country).append(", ");
        }
        return countyBuilder.substring(0, countyBuilder.length() - 2);
    }


}