package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        checkUserId(friendId);
        checkUserId(userId);
        userStorage.addFriend(userId, friendId);
        log.debug("Add" + userId + " & " + friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriend(userId, friendId);
        log.debug("Delete" + userId + " & " + friendId);
    }

    public List<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public User addUser(User user) {
        checkUserName(user);
        if (checkUser(user)) {
            log.debug(user.toString());
            userStorage.addUser(user);
        }
        return user;
    }

    public List<User> getAllUsers() {
        log.debug(userStorage.getAllUsers().toString());
        return new ArrayList<>(userStorage.getAllUsers());
    }

    public User updateUser(User user) {
        checkUserName(user);
        checkUserId(user.getId());
        if (checkUser(user)) {
            log.debug(user.toString());
            userStorage.updateUser(user);
        }
        return user;
    }

    public User getUser(Long id) {
        log.debug(id.toString());
        checkUserId(id);
        return userStorage.getUser(id);
    }

    private void checkUserId(Long id) {
        if (!userStorage.getUsersId().contains(id)) throw new UserNotFoundException(id);
    }

    private void checkUserName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) user.setName(user.getLogin());
    }

    public List<User> getCommon(Long otherId, Long id) {
        List<User> users = new ArrayList<>();
        Set<Long> s1 = new HashSet<>(userStorage.getFriendsId(otherId));
        Set<Long> s2 = new HashSet<>(userStorage.getFriendsId(id));
        s1.retainAll(s2);
        for (Long l : s1) {
            users.add(userStorage.getUser(l));
        }
        return users;
    }

    private boolean checkUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым");
        } else return true;
    }
}
