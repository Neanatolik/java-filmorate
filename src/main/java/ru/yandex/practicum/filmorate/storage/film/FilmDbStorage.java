package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
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
    final String ADD_FILM = "MERGE INTO FILMS (id, name, description, release_date, duration, mpa) values (?, ?, ?, ?, ?, ?);";
    final String ADD_GENRE = "INSERT INTO FILMS_GENRE (film_id, genre_id) values (?,?)";
    final String ADD_LIKE = "MERGE INTO LIKES (film_id, user_id) " + "KEY(film_id, user_id) values (?,?)";
    final String DELETE_LIKES = "DELETE FROM LIKES WHERE film_id = ?;";
    final String GET_GENRES = "SELECT * FROM FILMS_GENRE WHERE film_id = ?";
    final String UPDATE_FILM = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? WHERE id = ?";
    final String DELETE_GENRES = "DELETE FROM FILMS_GENRE WHERE film_id = ?;";
    final String GET_FILM = "SELECT * FROM FILMS WHERE id = ?";
    final String GET_FILMS = "SELECT * FROM FILMS";
    final String GET_LIKES = "SELECT * FROM LIKES WHERE film_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(ADD_FILM, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(ADD_GENRE, film.getId(), genre.getId());
        }
        for (Long id : film.getLikes()) {
            jdbcTemplate.update(ADD_LIKE, film.getId(), id);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(GET_FILMS, ((rs, rowNum) -> {
            return makeFilm(rs);
        }));
    }

    @Override
    public Film getFilm(Long id) {
        return jdbcTemplate.queryForObject(GET_FILM, (rs, rowNum) -> {
            return makeFilm(rs);
        }, id);
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update(DELETE_GENRES, film.getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(ADD_GENRE, film.getId(), genre.getId());
        }
        jdbcTemplate.update(DELETE_LIKES, film.getId());
        for (Long id : film.getLikes()) {
            jdbcTemplate.update(ADD_LIKE, film.getId(), id);
        }
        return film;
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> users = new HashMap<>();
        for (Film film : getAllFilms()) {
            users.put(film.getId(), film);
        }
        return users;
    }

    private List<Long> makeListOfLikes(Long idFilm) {
        return jdbcTemplate.query(GET_LIKES, ((rs, rowNum) -> rs.getLong("user_id")), idFilm);
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
        List<Genre> genreList = jdbcTemplate.query(GET_GENRES, ((rs, rowNum) -> {
            return makeGenre(rs);
        }), idFilm);
        return genreList;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        return new Genre(id, Genre.getGenreById(id).getName());
    }

}