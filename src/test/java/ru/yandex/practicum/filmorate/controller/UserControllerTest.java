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
    private static UserController userC;
    private static User user1;
    private static User user2;
    private static User user3;
    private static User user4;

    @BeforeAll
    public static void beforeAll() {
        user1 = new User("1","Login1", "123@", LocalDate.of(1994, Month.SEPTEMBER, 9));
        user2 = new User("2","Login1", "123", LocalDate.of(1994, Month.SEPTEMBER, 9));
        user3 = new User("3","Login1", "123@", LocalDate.of(2994, Month.SEPTEMBER, 9));
        user4 = new User("Nick Name","dolore", "mail@mail.ru", LocalDate.of(1946, Month.AUGUST, 20));
        userC = new UserController();
    }

    @Test
    public void testUserController() {
        assertEquals(user1, userC.addUser(user1));
        assertEquals(user4, userC.addUser(user4));
        assertThrows(
                FilmorateException.class,
                () -> userC.addUser(user2)
        );
        assertThrows(
                FilmorateException.class,
                () -> userC.addUser(user3)
        );
    }

}