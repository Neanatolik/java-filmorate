package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final String ADDFILM = "MERGE INTO FILMS (id, name, description, release_date, duration, mpa) values (?, ?, ?, ?, ?, ?);";
    private final String ADDGENRE = "INSERT INTO FILMS_GENRE (film_id, genre_id) values (?,?)";
    private final String ADDLIKE = "MERGE INTO LIKES (film_id, user_id) " + "KEY(film_id, user_id) values (?,?)";
    private final String DELETELIKES = "DELETE FROM LIKES WHERE film_id = ?;";
    private final String GETGENRES = "SELECT * FROM FILMS_GENRE WHERE film_id = ?";
    private final String UPDATEFILM = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? WHERE id = ?";
    private final String DELETEGENRES = "DELETE FROM FILMS_GENRE WHERE film_id = ?;";
    private final String GETFILM = "SELECT * FROM FILMS WHERE id = ?";
    private final String GETFILMS = "SELECT * FROM FILMS";
    private final String GETLIKES = "SELECT * FROM LIKES WHERE film_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(ADDFILM, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(ADDGENRE, film.getId(), genre.getId());
        }
        for (Long id : film.getLikes()) {
            jdbcTemplate.update(ADDLIKE, film.getId(), id);
        }
        log.debug(film.toString());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(GETFILMS, ((rs, rowNum) -> {
            return makeFilm(rs);
        }));
    }

    @Override
    public Film getFilm(Long id) {
        return jdbcTemplate.queryForObject(GETFILM, (rs, rowNum) -> {
            return makeFilm(rs);
        }, id);
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(UPDATEFILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update(DELETEGENRES, film.getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(ADDGENRE, film.getId(), genre.getId());
        }
        jdbcTemplate.update(DELETELIKES, film.getId());
        for (Long id : film.getLikes()) {
            jdbcTemplate.update(ADDLIKE, film.getId(), id);
        }
        log.debug(film.toString());
        return film;
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> users = new HashMap<>();
        for (Film film : getAllFilms()) {
            users.put(film.getId(), film);
        }
        log.debug(users.toString());
        return users;
    }

    private List<Long> makeListOfLikes(Long idFilm) {
        return jdbcTemplate.query(GETLIKES, ((rs, rowNum) -> rs.getLong("user_id")), idFilm);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Long idFilm = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int intMpa = rs.getInt("mpa");
        Film film = new Film(name, description, releaseDate, duration, Mpa.getMpaById(intMpa), makeGenreList(idFilm), new HashSet<>(makeListOfLikes(idFilm)));
        film.setId(idFilm);
        return film;
    }

    private List<Genre> makeGenreList(Long idFilm) {
        List<Genre> genreList = jdbcTemplate.query(GETGENRES, ((rs, rowNum) -> {
            return makeGenre(rs);
        }), idFilm);
        return genreList;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        return new Genre(id, Genre.getGenreById(id).getName());
    }

}