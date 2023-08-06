package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmServiceTest {
    private static final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    private static FilmService filmS;
    private static Film film1;
    private static Film film2;

    @BeforeAll
    public static void beforeAll() {
        film1 = new Film("1", "Описание1",
                LocalDate.of(2011, Month.APRIL, 28), 30);
        film2 = new Film("2", "Описание2",
                LocalDate.of(2011, Month.APRIL, 27), 60);
        filmS = new FilmService(filmStorage);
    }

    @Test
    public void testUserService() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        filmS.addLike(1L, 2L);
        assertEquals(1, film1.getLikes().size());
        filmS.deleteLike(1L, 2L);
        assertEquals(0, film1.getLikes().size());
    }

}