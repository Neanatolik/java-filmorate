package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Service
public class FilmService {

    private static final LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        checkFilmId(filmId);
        checkUserId(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkFilmId(filmId);
        checkUserId(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public Film addFilm(Film film) {
        if (checkFilm(film)) {
            setGenreFromId(film);
            log.debug(film.toString());
            filmStorage.addFilm(film);
        }
        return film;
    }

    private void setGenreFromId(Film film) {
        LinkedHashSet<Genres> genresSet = new LinkedHashSet<>();
        if (Objects.nonNull(film.getGenres())) {
            genresSet = film.getGenres();
        }
        film.setGenres(genresSet);
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getFilms());
    }

    public Film getFilm(Long id) {
        log.debug(id.toString());
        checkFilmId(id);
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        checkFilmId(film.getId());
        setGenreFromId(film);
        if (checkFilm(film)) {
            log.debug(film.toString());
            filmStorage.updateFilm(film);
        }
        return film;
    }

    public List<Film> getPopular(int count) {
        if (count > filmStorage.getFilms().size()) count = filmStorage.getFilms().size();
        return filmStorage.getPopular(count).subList(0, count);
    }

    private void checkFilmId(Long id) {
        if (!filmStorage.getFilmsId().contains(id)) throw new FilmNotFoundException(id);
    }

    private void checkUserId(Long id) {
        if (id < 1) throw new UserNotFoundException(id);
    }

    private boolean checkFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Имя фильма не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не должно быть больше 200 символов");
        } else if (film.getReleaseDate().isBefore(minDate)) {
            throw new ValidationException("Дата фильма не может быть раньше 28.12.1985");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность фильма должна быть положительной");
        } else return true;
    }

}
