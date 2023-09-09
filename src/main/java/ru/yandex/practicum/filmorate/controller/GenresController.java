package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.List;

@RestController
public class GenresController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final GenresService genresService;

    @Autowired
    public GenresController(GenresService genresService) {
        this.genresService = genresService;
    }

    @GetMapping("/genres")
    public List<Genres> getGenres() {
        log.info("GET /genres");
        return genresService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genres getGenre(@PathVariable Integer id) {
        log.info("GET /genres/" + id);
        return genresService.getGenreById(id);
    }
}
