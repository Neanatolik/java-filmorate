package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    void addLike(Long id, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getFilms();

    Film getFilm(Long id);

    List<Film> getPopular(int count);
    List<Long> getFilmsId();

    Film updateFilm(Film film);

}
