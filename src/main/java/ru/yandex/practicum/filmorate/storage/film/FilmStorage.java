package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    Genres getGenre(int id);

    List<Genres> getGenres();

    Film addFilm(Film film);

    Long amountOfGenres();

    void addLike(Long id, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getAllFilms();

    Film getFilm(Long id);

    List<Film> getPopular(int count);

    Film updateFilm(Film film);

    List<Long> getFilmsId();

    List<Mpa> getMpas();

    Long amountOfMpas();

    Mpa getMpa(int id);
}
