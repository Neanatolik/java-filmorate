DROP TABLE users IF EXISTS CASCADE;
CREATE TABLE IF NOT EXISTS users (
	id int PRIMARY KEY,
	name varchar,
	login varchar,
	email varchar,
	birthday timestamp
);

DROP TABLE films IF EXISTS CASCADE;
CREATE TABLE IF NOT EXISTS films (
    id int PRIMARY KEY,
    name varchar,
    description varchar,
    release_date timestamp,
    duration int,
    mpa int
);

DROP TABLE friends IF EXISTS CASCADE;
CREATE TABLE IF NOT EXISTS friends (
    request_user_id int REFERENCES users(id),
    response_user_id int REFERENCES users(id)
);

DROP TABLE likes IF EXISTS CASCADE;
CREATE TABLE IF NOT EXISTS likes (
    film_id int REFERENCES films(id),
    user_id int REFERENCES users(id)
);

DROP TABLE films_genre IF EXISTS CASCADE;
CREATE TABLE IF NOT EXISTS films_genre(
    film_id int,
	genre_id int,
	foreign key (film_id) REFERENCES films(id)
);

