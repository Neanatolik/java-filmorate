package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class GenresService {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final GenresStorage genresStorage;

    @Autowired
    public GenresService(GenresStorage genresStorage) {
        this.genresStorage = genresStorage;
    }
    public Genres getGenreById(int id) {
        checkGenre(id);
        log.debug(Integer.toString(id));
        return genresStorage.getGenre(id);
    }

    private void checkGenre(int id) {
        if (genresStorage.amountOfGenres() < id) throw new GenreNotFoundException(id);
    }

    public List<Genres> getGenres() {
        return genresStorage.getGenres();
    }
}
