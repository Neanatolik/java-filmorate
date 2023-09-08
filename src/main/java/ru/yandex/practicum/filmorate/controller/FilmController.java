package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmDbStorage filmStorage) {
        this.filmService = new FilmService(filmStorage);
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("POST /films");
        return filmService.addFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("GET /films");
        return filmService.getAllFilms();
    }

    @GetMapping("/genres")
    public List<Genres> getGenres() {
        log.info("GET /genres");
        return filmService.getGenres();
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpaRatings() {
        log.info("GET /mpa");
        return filmService.getMpas();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("GET /films/" + id);
        return filmService.getFilm(id);
    }

    @GetMapping("/genres/{id}")
    public Genres getGenre(@PathVariable Integer id) {
        log.info("GET /genres/" + id);
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaRating(@PathVariable int id) {
        log.info("GET /mpa" + id);
        return filmService.getMpaById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("GET /films/popular");
        return filmService.getPopular(count);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("PUT /films");
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("PUT /films/" + id + "/like/" + userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("DELETE /films/" + id + "/like/" + userId);
        filmService.deleteLike(id, userId);
    }

}
