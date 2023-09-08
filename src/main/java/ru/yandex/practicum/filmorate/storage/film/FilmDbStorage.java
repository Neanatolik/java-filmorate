package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
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

    private final String addGenre = "INSERT INTO FILMS_GENRE (film_id, genre_id) values (?,?)";
    private final String getFilms = "SELECT * FROM FILMS f LEFT JOIN FILMS_GENRE fg ON f.ID = fg.FILM_ID LEFT JOIN  GENRES g ON fg.GENRE_ID = g.GENRE_ID";
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genres getGenre(int id) {
        String getGenre = "SELECT * FROM GENRES WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(getGenre, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public List<Genres> getGenres() {
        String getAllGenres = "SELECT * FROM GENRES";
        return jdbcTemplate.query(getAllGenres, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films").usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        for (Genres genres : film.getGenres()) {
            jdbcTemplate.update(addGenre, film.getId(), genres.getId());
        }
        log.debug(film.toString());
        return film;
    }

    @Override
    public Long amountOfGenres() {
        String getAmountOfGenres = "SELECT COUNT(genre_id) FROM GENRES";
        return jdbcTemplate.queryForObject(getAmountOfGenres, Long.class);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String addLike = "MERGE INTO LIKES (film_id, user_id) KEY(film_id, user_id) values (?,?);";
        jdbcTemplate.update(addLike, filmId, userId);
        String increaseRate = "UPDATE FILMS SET rate = rate + 1 WHERE id = ?";
        jdbcTemplate.update(increaseRate, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String deleteLike = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
        String decreaseRate = "UPDATE FILMS SET rate = rate + 1 WHERE id = ?";
        jdbcTemplate.update(decreaseRate, filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        Map<Long, Film> mapFilmId = new HashMap<>();
        jdbcTemplate.query(getFilms, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                getListWithSet(rs, mapFilmId);
            }
        });
        return new ArrayList<>(mapFilmId.values());
    }

    private Map<Long, Film> getListWithSet(ResultSet rs, Map<Long, Film> setMap) throws SQLException {
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
        return setMap;
    }

    private Film makeFilm(ResultSet rs, Genres genres) throws SQLException {
        Long idFilm = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int rate = rs.getInt("rate");
        int mpa = rs.getInt("mpa");
        Film film;
        if (Objects.isNull(genres) || Objects.isNull(genres.getName())) {
            film = new Film(idFilm, name, description, releaseDate, duration, rate, getMpa(mpa), new LinkedHashSet<>());
        } else {
            film = new Film(idFilm, name, description, releaseDate, duration, rate, getMpa(mpa), new LinkedHashSet<>(List.of(genres)));
        }
        film.setId(idFilm);
        return film;
    }

    @Override
    public Film getFilm(Long id) {
        Map<Long, Film> mapFilmId = new HashMap<>();
        jdbcTemplate.query(getFilms, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                getListWithSet(rs, mapFilmId);
            }
        });
        return mapFilmId.get(id);
    }

    @Override
    public List<Film> getPopular(int count) {
        String getPopular = "SELECT * FROM FILMS f LEFT JOIN FILMS_GENRE fg ON f.ID = fg.FILM_ID LEFT JOIN  GENRES g ON fg.GENRE_ID = g.GENRE_ID ORDER BY rate DESC LIMIT ?";
        return jdbcTemplate.query(getPopular, ((rs, rowNum) -> makeFilm(rs, new Genres(rs.getInt("genre_id"), rs.getString("genre_name")))), count);
    }

    @Override
    public Film updateFilm(Film film) {
        String updateFilm = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ?, rate = ? WHERE id = ?";
        jdbcTemplate.update(updateFilm, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        String deleteGenres = "DELETE FROM FILMS_GENRE WHERE film_id = ?";
        jdbcTemplate.update(deleteGenres, film.getId());
        for (Genres genres : film.getGenres()) {
            jdbcTemplate.update(addGenre, film.getId(), genres.getId());
        }
        log.debug(film.toString());
        return film;
    }

    @Override
    public List<Long> getFilmsId() {
        String getFilmsId = "SELECT id FROM FILMS";
        return jdbcTemplate.query(getFilmsId, (rs, rowNum) -> rs.getLong("id"));
    }

    @Override
    public List<Mpa> getMpas() {
        String getAllMpa = "SELECT * FROM MPA_RATING";
        return jdbcTemplate.query(getAllMpa, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
    }

    @Override
    public Long amountOfMpas() {
        String getAmountOfMpa = "SELECT COUNT(mpa_id) FROM MPA_RATING";
        return jdbcTemplate.queryForObject(getAmountOfMpa, Long.class);
    }

    @Override
    public Mpa getMpa(int id) {
        String getMpa = "SELECT * FROM MPA_RATING WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(getMpa, (rs, rowNum) -> makeMpa(rs), id);
    }

    private Genres makeGenre(ResultSet rs) throws SQLException {
        return new Genres(rs.getInt("genre_id"), rs.getString("genre_name"));
    }

}