package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long friend, Long user) {
        checkUserId(friend);
        checkUserId(user);
        userStorage.getUser(user).addFriend(friend);
        userStorage.getUser(friend).addFriend(user);
    }

    public void deleteFriend(Long friend, Long user) {
        userStorage.getUser(user).getFriends().remove(friend);
        userStorage.getUser(friend).getFriends().remove(user);
    }

    public List<User> getFriends(Long userId) {
        List<User> users = new ArrayList<>();
        for (Long l : userStorage.getUser(userId).getFriends()) {
            users.add(userStorage.getUser(l));
        }
        return users;
    }

    private void checkUserId(Long id) {
        if (!userStorage.getUsers().containsKey(id)) throw new UserNotFoundException(id);
    }

    public List<User> getCommon(Long otherId, Long id) {
        List<User> users = new ArrayList<>();
        Set<Long> s1 = new HashSet<>(userStorage.getUser(id).getFriends());
        Set<Long> s2 = new HashSet<>(userStorage.getUser(otherId).getFriends());
        s1.retainAll(s2);
        for (Long l : s1) {
            users.add(userStorage.getUser(l));
        }
        return users;
    }
}
