package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmorateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private static final LocalDate localDate = LocalDate.of(1895, Month.DECEMBER, 28);
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        if (checkFilm(film)) {
            log.debug(film.toString());
            films.put(film.getId(), film);
        }
        return film;
    }

    @GetMapping("/films")
    public String getFilms() {
        return films.toString();
    }

    @PutMapping("/films")
    public void updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.debug(film.toString());
            films.put(film.getId(), film);
            System.out.println(film);
        }
    }

    private boolean checkFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new FilmorateException("Имя фильма не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new FilmorateException("Описание фильма не должно быть больше 200 символов");
        } else if (film.getReleaseDate().isBefore(localDate)) {
            throw new FilmorateException("Дата фильма не может быть раньше 28.12.1985");
        } else if (film.getDuration().isNegative()) {
            throw new FilmorateException("Длительность фильма должна быть положительной");
        } else return true;
    }

}
