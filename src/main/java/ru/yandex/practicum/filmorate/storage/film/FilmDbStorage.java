package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(jdbcTemplate).withTableName("films").usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        addGenre(film);
        log.debug(film.toString());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String updateFilm = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ?, rate = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(updateFilm, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        String deleteGenres = "DELETE FROM FILMS_GENRE WHERE film_id = ?";
        jdbcTemplate.update(deleteGenres, film.getId());
        addGenre(film);
        log.debug(film.toString());
        return film;
    }

    @Override
    public Film getFilm(Long id) {
        Map<Long, Film> mapFilmId = new HashMap<>();
        String getFilm = "SELECT * " +
                "FROM FILMS f " +
                "LEFT JOIN FILMS_GENRE fg ON f.ID = fg.FILM_ID " +
                "LEFT JOIN  GENRES g ON fg.GENRE_ID = g.GENRE_ID " +
                "LEFT JOIN  MPA_RATING mr ON f.MPA = mr.MPA_ID " +
                "WHERE f.id = ?";
        jdbcTemplate.query(getFilm, rs -> {
            getListWithSet(rs, mapFilmId);
        }, id);
        return mapFilmId.get(id);
    }

    @Override
    public List<Film> getFilms() {
        Map<Long, Film> mapFilmId = new HashMap<>();
        String getFilms = "SELECT * " +
                "FROM FILMS f " +
                "LEFT JOIN FILMS_GENRE fg ON f.ID = fg.FILM_ID " +
                "LEFT JOIN  GENRES g ON fg.GENRE_ID = g.GENRE_ID " +
                "LEFT JOIN MPA_RATING mr ON f.MPA = mr.MPA_ID";
        jdbcTemplate.query(getFilms, rs -> {
            getListWithSet(rs, mapFilmId);
        });
        return new ArrayList<>(mapFilmId.values());
    }

    @Override
    public List<Film> getPopular(int count) {
        String getPopular = "SELECT * " +
                "FROM FILMS f " +
                "LEFT JOIN FILMS_GENRE fg ON f.ID = fg.FILM_ID " +
                "LEFT JOIN  GENRES g ON fg.GENRE_ID = g.GENRE_ID " +
                "LEFT JOIN MPA_RATING mr ON f.MPA = mr.MPA_ID " +
                "ORDER BY rate DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(getPopular, ((rs, rowNum) -> makeFilm(rs, new Genres(rs.getInt("genre_id"), rs.getString("genre_name")))), count);
    }

    @Override
    public List<Long> getFilmsId() {
        String getFilmsId = "SELECT id FROM FILMS";
        return jdbcTemplate.query(getFilmsId, (rs, rowNum) -> rs.getLong("id"));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String addLike = "MERGE INTO LIKES (film_id, user_id) KEY(film_id, user_id) values (?,?);";
        jdbcTemplate.update(addLike, filmId, userId);
        String increaseRate = "UPDATE FILMS SET rate = rate + 1 " +
                "WHERE id = ?";
        jdbcTemplate.update(increaseRate, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String deleteLike = "DELETE FROM LIKES " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
        String decreaseRate = "UPDATE FILMS SET rate = rate + 1 WHERE id = ?";
        jdbcTemplate.update(decreaseRate, filmId);
    }

    private void addGenre(Film film) {
        for (Genres genres : film.getGenres()) {
            String addGenre = "INSERT INTO FILMS_GENRE (film_id, genre_id) values (?,?)";
            jdbcTemplate.update(addGenre, film.getId(), genres.getId());
        }
    }

    private void getListWithSet(ResultSet rs, Map<Long, Film> setMap) throws SQLException {
        Long filmId = rs.getLong("id");
        int genreId = rs.getInt("genre_id");
        String genreName = rs.getString("genre_name");
        Genres genres;
        if (!Objects.isNull(genreName)) {
            genres = new Genres(genreId, genreName);
            if (setMap.containsKey(filmId)) {
                setMap.get(filmId).getGenres().add(genres);
            } else {
                setMap.put(filmId, makeFilm(rs, genres));
            }
        } else setMap.put(filmId, makeFilm(rs, null));
    }

    private Film makeFilm(ResultSet rs, Genres genres) throws SQLException {
        Long idFilm = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int rate = rs.getInt("rate");
        int mpa = rs.getInt("mpa");
        String mpaName = rs.getString("mpa_name");
        Film film;
        if (Objects.isNull(genres) || Objects.isNull(genres.getName())) {
            film = new Film(idFilm, name, description, releaseDate, duration, rate,
                    new Mpa(mpa, mpaName), new LinkedHashSet<>());
        } else {
            film = new Film(idFilm, name, description, releaseDate, duration, rate,
                    new Mpa(mpa, mpaName), new LinkedHashSet<>(List.of(genres)));
        }
        film.setId(idFilm);
        return film;
    }

}