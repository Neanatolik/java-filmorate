package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmorateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    static FilmController filmController;
    static Film film1;
    static Film film2;
    static Film film3;
    static Film film4;

    @BeforeAll
    public static void beforeAll() {
        film1 = new Film(1, "1", "Описание1",
                LocalDate.of(2011, Month.APRIL, 28), Duration.of(30, ChronoUnit.MINUTES));
        film2 = new Film(2, " ", "Описание2",
                LocalDate.of(1999, Month.AUGUST, 2), Duration.of(90, ChronoUnit.MINUTES));
        film3 = new Film(3, "3", "Описание3",
                LocalDate.of(1700, Month.FEBRUARY, 14), Duration.of(90, ChronoUnit.MINUTES));
        film4 = new Film(4, "4", "YYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYO" +
                "YYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYO" +
                "YYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYOYYYYO"
                ,
                LocalDate.of(1999, Month.JULY, 29), Duration.of(90, ChronoUnit.MINUTES));
        filmController = new FilmController();
    }

    @Test
    public void test() {
        assertEquals(film1, filmController.addFilm(film1));
        final FilmorateException exception = assertThrows(
                FilmorateException.class,
                () -> filmController.addFilm(film2)
        );
        final FilmorateException exception2 = assertThrows(
                FilmorateException.class,
                () -> filmController.addFilm(film3)
        );
        final FilmorateException exception3 = assertThrows(
                FilmorateException.class,
                () -> filmController.addFilm(film4)
        );
    }

}