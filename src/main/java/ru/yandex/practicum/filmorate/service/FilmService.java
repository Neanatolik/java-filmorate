package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.enums.Genres;
import ru.yandex.practicum.filmorate.enums.MpaRating;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
public class FilmService {

    private static final LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;
    private Long id = 0L;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long id, Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        Film film = filmStorage.getFilm(id);
        film.addLike(userId);
        filmStorage.updateFilm(film);
    }

    public void deleteLike(Long id, Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        Film film = filmStorage.getFilm(id);
        film.deleteLike(userId);
        filmStorage.updateFilm(film);
    }

    public Film addFilm(Film film) {
        if (checkFilm(film)) {
            if (Objects.nonNull(film.getGenres())) checkDuplicateGenre(film);
            setGenreFromId(film);
            setMpaFromId(film);
            setLikesFromId(film);
            film.setId(getNextId());
            log.debug(film.toString());
            filmStorage.addFilm(film);
        }
        return film;
    }

    private void checkDuplicateGenre(Film film) {
        Set<Genre> set = new LinkedHashSet<>(film.getGenres());
        film.getGenres().clear();
        film.getGenres().addAll(set);
    }

    private void setLikesFromId(Film film) {
        Set<Long> likesSet = new HashSet<>();
        if (Objects.nonNull(film.getLikes())) {
            likesSet.addAll(film.getLikes());
        }
        film.setLikes(likesSet);
    }

    private void setGenreFromId(Film film) {
        List<Genre> genreList = new ArrayList<>();
        if (Objects.nonNull(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                genreList.add(Genre.getGenreById(genre.getId()));
            }
        }
        film.setGenres(genreList);
    }

    private void setMpaFromId(Film film) {
        int mpaId = film.getMpa().getId();
        for (MpaRating mpaRating : MpaRating.values()) {
            if (mpaRating.getId() == mpaId) {
                film.getMpa().setId(mpaRating.getId());
                film.getMpa().setName(mpaRating.getName());
            }
        }
    }

    public List<Film> getAllFilms() {
        log.debug(filmStorage.getFilms().toString());
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film getFilm(Long id) {
        log.debug(id.toString());
        checkFilmId(id);
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        checkFilmId(film.getId());
        if (Objects.nonNull(film.getGenres())) checkDuplicateGenre(film);
        setGenreFromId(film);
        setMpaFromId(film);
        setLikesFromId(film);
        if (checkFilm(film)) {
            log.debug(film.toString());
            filmStorage.updateFilm(film);
        }
        return film;
    }

    public List<Film> getPopular(int count) {
        if (filmStorage.getFilms().size() < count) count = filmStorage.getFilms().size();
        List<Film> popularFilms = new LinkedList<>(filmStorage.getAllFilms());
        popularFilms.sort((Film film1, Film film2) -> {
            return Integer.compare(film2.getAmountOfLikes(), film1.getAmountOfLikes());
        });
        return popularFilms.subList(0, count);
    }

    private void checkFilmId(Long id) {
        if (!filmStorage.getFilms().containsKey(id)) throw new FilmNotFoundException(id);
    }

    private void checkUserId(Long id) {
        if (id < 1) throw new UserNotFoundException(id);
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

    public Genres getGenreById(Long id) {
        checkGenre(id);
        for (Genres g : Genres.values())
            if (g.getId() == id) return g;
        return Genres.NoGenre;
    }

    private void checkGenre(Long id) {
        if (Genres.values().length <= id) throw new GenreNotFoundException(id);
    }

    public MpaRating getMpaById(Long id) {
        checkMpa(id);
        for (MpaRating mpaRating : MpaRating.values())
            if (mpaRating.getId() == id) return mpaRating;
        return MpaRating.PG;
    }

    private void checkMpa(Long id) {
        if (MpaRating.values().length <= id) throw new MpaNotFoundException(id);
    }

}
