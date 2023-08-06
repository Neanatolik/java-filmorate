package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.LikeComparator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.LinkedList;
import java.util.List;

@Service
public class FilmService {

    private static final LikeComparator likeComparator = new LikeComparator();
    private final FilmStorage filmStorage;

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

    public List<Film> getPopular(int count) {
        if (filmStorage.getFilms().size() < count) count = filmStorage.getFilms().size();
        List<Film> popularFilms = new LinkedList<>(filmStorage.getAllFilms());
        popularFilms.sort(likeComparator);
        return popularFilms.subList(0, count);
    }

    private void checkFilmId(Long id) {
        if (!filmStorage.getFilms().containsKey(id)) throw new FilmNotFoundException(id);
    }

    private void checkUserId(Long id) {
        if (id < 1) throw new UserNotFoundException(id);
    }

}
