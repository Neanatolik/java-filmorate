package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(Long id);

    Film updateFilm(Film film);

    Map<Long, Film> getFilms();
}
