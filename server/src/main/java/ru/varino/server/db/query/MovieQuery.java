package ru.varino.server.db.query;

public enum MovieQuery {
    FIND_ALL("select * from movies"),
    SAVE_PERSON("insert into movie " +
            "(owner_id, name, cord_x, cord_y, oscars_count, total_box_office, tagline, genre, director_name, director_birthday, director_weight, director_nationality) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id"),
    REMOVE_BY_ID("delete from movies where id = ?");


    private String sql;

    MovieQuery(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
