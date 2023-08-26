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
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FilmService {

    private static final LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;
    private Long id = 0L;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long id, Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmStorage.getFilm(id).addLike(userId);
    }

    public void deleteLike(Long id, Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmStorage.getFilm(id).deleteLike(userId);
    }

    public Film addFilm(Film film) {
        if (checkFilm(film)) {
            film.setId(getNextId());
            log.debug(film.toString());
            filmStorage.addFilm(film);
        }
        return film;
    }

    public List<Film> getAllFilms() {
        log.debug(filmStorage.getFilms().toString());
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film getFilm(Long id) {
        log.debug(id.toString());
        checkFilmId(id);
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        checkFilmId(film.getId());
        if (checkFilm(film)) {
            log.debug(film.toString());
            filmStorage.addFilm(film);
        }
        return film;
    }

    public List<Film> getPopular(int count) {
        if (filmStorage.getFilms().size() < count) count = filmStorage.getFilms().size();
        List<Film> popularFilms = new LinkedList<>(filmStorage.getAllFilms());
        popularFilms.sort((Film film1, Film film2) -> {
            return Integer.compare(film2.getAmountOfLikes(), film1.getAmountOfLikes());
        });
        return popularFilms.subList(0, count);
    }

    private void checkFilmId(Long id) {
        if (!filmStorage.getFilms().containsKey(id)) throw new FilmNotFoundException(id);
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

    private Long getNextId() {
        return ++id;
    }

}
