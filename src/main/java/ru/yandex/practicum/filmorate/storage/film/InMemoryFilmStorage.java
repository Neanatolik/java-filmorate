package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 0L;

    @Override
    public Film addFilm(Film film) {
        if (checkFilm(film)) {
            film.setId(getNextId());
            log.debug(film.toString());
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug(films.toString());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id) {
        log.debug(id.toString());
        checkFilmId(id);
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.debug(film.toString());
            films.put(film.getId(), film);
        } else throw new FilmNotFoundException(film.getId());
        return film;
    }

    @Override
    public Map<Long, Film> getFilms() {
        log.debug(films.toString());
        return films;
    }

    private void checkFilmId(Long id) {
        if (!films.containsKey(id)) throw new FilmNotFoundException(id);
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
