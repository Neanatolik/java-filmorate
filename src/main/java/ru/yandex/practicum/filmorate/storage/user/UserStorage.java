package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    List<User> getAllUsers();

    User getUser(Long id);

    Map<Long, User> getUsers();

    User updateUser(User user);

    void deleteFriend(Long userId, Long friendId);
}
