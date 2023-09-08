package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
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
            setMpaFromId(film);
            setRate(film);
            log.debug(film.toString());
            filmStorage.addFilm(film);
        }
        return film;
    }

    private void setRate(Film film) {
        if (Objects.isNull(film.getRate())) film.setRate(0);
    }

    private void setGenreFromId(Film film) {
        LinkedHashSet<Genres> genresSet = new LinkedHashSet<>();
        if (Objects.nonNull(film.getGenres())) {
            for (Genres genres : film.getGenres()) {
                genresSet.add(getGenreById(genres.getId()));
            }
        }
        film.setGenres(genresSet);
    }

    private void setMpaFromId(Film film) {
        film.setMpa(filmStorage.getMpa(film.getMpa().getId()));
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAllFilms());
    }

    public Film getFilm(Long id) {
        log.debug(id.toString());
        checkFilmId(id);
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        checkFilmId(film.getId());
        setGenreFromId(film);
        setMpaFromId(film);
        setRate(film);
        if (checkFilm(film)) {
            log.debug(film.toString());
            filmStorage.updateFilm(film);
        }
        return film;
    }

    public List<Film> getPopular(int count) {
        if (count > filmStorage.getAllFilms().size()) count = filmStorage.getAllFilms().size();
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

    public Genres getGenreById(int id) {
        checkGenre(id);
        return filmStorage.getGenre(id);
    }

    private void checkGenre(int id) {
        if (filmStorage.amountOfGenres() < id) throw new GenreNotFoundException(id);
    }

    public Mpa getMpaById(int id) {
        checkMpa(id);
        return filmStorage.getMpa(id);
    }

    private void checkMpa(int id) {
        if (filmStorage.amountOfMpas() < id) throw new MpaNotFoundException(id);
    }

    public List<Genres> getGenres() {
        return filmStorage.getGenres();
    }

    public List<Mpa> getMpas() {
        return filmStorage.getMpas();
    }
}
