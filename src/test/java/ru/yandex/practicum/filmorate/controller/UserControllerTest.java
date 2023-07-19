package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmorateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    static UserController userC;
    static User user1;
    static User user2;
    static User user3;
    static User user4;

    @BeforeAll
    public static void beforeAll() {
        user1 = new User(1, "123@", "Login1", "Name1", LocalDate.of(1994, Month.SEPTEMBER, 9));
        user2 = new User(2, "123", "Login1", "Name1", LocalDate.of(1994, Month.SEPTEMBER, 9));
        user3 = new User(3, "123@", "Login1", "Name1", LocalDate.of(2994, Month.SEPTEMBER, 9));
        userC = new UserController();
    }

    @Test
    public void test() {
        assertEquals(user1, userC.addUser(user1));
        final FilmorateException exception = assertThrows(
                FilmorateException.class,
                () -> userC.addUser(user2)
        );
        final FilmorateException exception2 = assertThrows(
                FilmorateException.class,
                () -> userC.addUser(user3)
        );
    }

}