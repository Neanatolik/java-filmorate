package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
public class MpaController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpaRatings() {
        log.info("GET /mpa");
        return mpaService.getMpas();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaRating(@PathVariable int id) {
        log.info("GET /mpa" + id);
        return mpaService.getMpaById(id);
    }

}
