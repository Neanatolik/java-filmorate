package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmorateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private static FilmController filmController;
    private static Film film1;
    private static Film film2;
    private static Film film3;
    private static Film film4;

    @BeforeAll
    public static void beforeAll() {
        film1 = new Film("1", "Описание1",
                LocalDate.of(2011, Month.APRIL, 28), 30);
        film2 = new Film("2", " ", LocalDate.of(1999, Month.AUGUST, 2), -30);
        film3 = new Film("3", "3", LocalDate.of(1700, Month.FEBRUARY, 14), 30);
        film4 = new Film("4", "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY",
                LocalDate.of(1999, Month.JULY, 29), 90);
        filmController = new FilmController();
    }

    @Test
    public void testFilmController() {
        assertEquals(film1, filmController.addFilm(film1));
        assertThrows(
                FilmorateException.class,
                () -> filmController.addFilm(film2)
        );
        assertThrows(
                FilmorateException.class,
                () -> filmController.addFilm(film3)
        );
        assertThrows(
                FilmorateException.class,
                () -> filmController.addFilm(film4)
        );
    }

}