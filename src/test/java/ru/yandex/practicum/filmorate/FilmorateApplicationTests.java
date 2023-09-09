package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenresStorage genresStorage;
    private final MpaStorage mpaStorage;

    @Test
    public void testUser() {
        User user1 = new User("1", "Login1", "1@",
                LocalDate.of(1991, Month.SEPTEMBER, 1));
        User user2 = new User("2", "Login2", "2@",
                LocalDate.of(1992, Month.SEPTEMBER, 2));
        assertEquals(user1, userStorage.addUser(user1));
        assertEquals(user2, userStorage.addUser(user2));
        assertEquals(user1, userStorage.getUser(3L));
        User user3 = new User("3", "Login3", "3@",
                LocalDate.of(1993, Month.SEPTEMBER, 3));
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
        List<Genres> genresList = new ArrayList<>();
        genresList.add(genresStorage.getGenre(1));
        java.util.Set<Long> list1 = new HashSet<>();
        list1.add(2L);
        Set<Long> list2 = new HashSet<>();
        list1.add(1L);

        Film film1 = new Film(1L, "Имя1", "Описание1",
                LocalDate.of(2011, Month.APRIL, 28), 1, 1, mpaStorage.getMpa(1), new LinkedHashSet<>(genresList));
        Film film2 = new Film(2L, "Имя2", "Описание2",
                LocalDate.of(1999, Month.AUGUST, 2), 2, 1, mpaStorage.getMpa(1), new LinkedHashSet<>(genresList));
        Film film3 = new Film(3L, "Имя3", "Описание3",
                LocalDate.of(1999, Month.AUGUST, 2), 3, 1, mpaStorage.getMpa(1), new LinkedHashSet<>(genresList));
        film1.setId(1L);
        film2.setId(2L);
        assertEquals(film1, filmStorage.addFilm(film1));
        assertEquals(film2, filmStorage.addFilm(film2));
        assertEquals(film1, filmStorage.getFilms().get(0));
        assertEquals(film2, filmStorage.getFilms().get(1));
        assertEquals(film1, filmStorage.getFilm(1L));
        film3.setId(2L);
        assertEquals(film3, filmStorage.updateFilm(film3));
        assertEquals(film3, filmStorage.getFilm(2L));

    }

}
