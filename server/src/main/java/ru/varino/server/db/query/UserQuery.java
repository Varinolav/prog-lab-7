package ru.varino.server.db.query;

public enum UserQuery {
    SAVE_USER("insert into users(username, password) values (?, ?)"),
    EXISTS_BY_USERNAME("select count(*) from users where username = ?"),
    FIND_ALL("select * from users"),
    FIND_BY_USERNAME("select * from users where username = ?"),
    FIND_BY_ID("select * from users where id = ?"),
    FIND_BY_USERNAME_PASSWORD("select * from users where username = ? and password = ?");


    private String sql;

    UserQuery(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
