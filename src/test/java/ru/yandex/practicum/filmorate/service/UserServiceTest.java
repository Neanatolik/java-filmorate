package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private static UserService userS;
    private static final InMemoryUserStorage userStorage = new InMemoryUserStorage();
    private static User user1;
    private static User user4;

    @BeforeAll
    public static void beforeAll() {
        user1 = new User("1", "Login1", "123@", LocalDate.of(1994, Month.SEPTEMBER, 9));
        user4 = new User("Nick Name", "dolore", "mail@mail.ru", LocalDate.of(1946, Month.AUGUST, 20));
        userS = new UserService(userStorage);
    }

    @Test
    public void testUserService() {
        userStorage.addUser(user1);
        userStorage.addUser(user4);
        userS.addFriend(1L,2L);
        assertEquals(1L, userS.getFriends(2L).get(0).getId());
        userS.deleteFriend(1L,2L);
        assertTrue(userS.getFriends(2L).isEmpty());
    }

}