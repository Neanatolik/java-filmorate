package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    List<User> getAllUsers();

    User getUser(Long id);

    User updateUser(User user);

    void deleteFriend(Long userId, Long friendId);

    void addFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<Long> getUsersId();

    List<Long> getFriendsId(Long otherId);
}
