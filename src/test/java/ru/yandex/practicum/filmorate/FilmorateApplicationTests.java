package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testUser() {
        User user1 = new User("1", "Login1", "1@",
                LocalDate.of(1991, Month.SEPTEMBER, 1));
        User user2 = new User("2", "Login2", "2@",
                LocalDate.of(1992, Month.SEPTEMBER, 2));
        User user3 = new User("3", "Login3", "3@",
                LocalDate.of(1993, Month.SEPTEMBER, 3));
        user1.setId(1L);
        user2.setId(2L);
        assertEquals(user1, userStorage.addUser(user1));
        assertEquals(user2, userStorage.addUser(user2));
        assertEquals(user1, userStorage.getAllUsers().get(0));
        assertEquals(user2, userStorage.getAllUsers().get(1));
        assertEquals(user1, userStorage.getUser(1L));
        user3.setId(3L);
        assertEquals(user3, userStorage.updateUser(user3));

    }

    @Test
    public void testFilm() {
        User user1 = new User("1", "Login1", "1@",
                LocalDate.of(1991, Month.SEPTEMBER, 1));
        User user2 = new User("2", "Login2", "2@",
                LocalDate.of(1992, Month.SEPTEMBER, 2));
        user1.setId(1L);
        user2.setId(2L);
        assertEquals(user1, userStorage.addUser(user1));
        assertEquals(user2, userStorage.addUser(user2));
        List<Genre> genreList = new ArrayList<>();
        genreList.add(Genre.getGenreById(1));
        java.util.Set<Long> list1 = new HashSet<>();
        list1.add(2L);
        Set<Long> list2 = new HashSet<>();
        list1.add(1L);

        Film film1 = new Film("1", "Описание1",
                LocalDate.of(2011, Month.APRIL, 28), 1, Mpa.getMpaById(1), genreList, list1);
        Film film2 = new Film("2", "Описание2",
                LocalDate.of(1999, Month.AUGUST, 2), 2, Mpa.getMpaById(2), genreList, list2);
        Film film3 = new Film("3", "Описание3",
                LocalDate.of(1999, Month.AUGUST, 2), 3, Mpa.getMpaById(3), genreList, list2);
        film1.setId(1L);
        film2.setId(2L);
        assertEquals(film1, filmStorage.addFilm(film1));
        assertEquals(film2, filmStorage.addFilm(film2));
        assertEquals(film1, filmStorage.getAllFilms().get(0));
        assertEquals(film2, filmStorage.getAllFilms().get(1));
        assertEquals(film1, filmStorage.getFilm(1L));
        film3.setId(2L);
        assertEquals(film3, filmStorage.updateFilm(film3));
        assertEquals(film3, filmStorage.getFilm(2L));

    }

}
