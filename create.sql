create table users
(
    id       serial primary key,
    username text,
    password text
);

create table movie
(
    id                   serial primary key,
    owner_id             int references users (id),
    name                 text not null,
    cord_x               float8  not null,
    cord_y               float8  not null,
    creation_date        timestamp default now(),
    oscars_count         int check (oscars_count > 0),
    total_box_office     int check (total_box_office > 0),
    tagline              text,
    genre                text not null,
    director_name        text not null,
    director_birthday    time,
    director_weight      int,
    director_nationality text not null
);