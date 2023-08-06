package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 0L;

    @Override
    public User addUser(User user) {
        checkUserName(user);
        if (checkUser(user)) {
            user.setId(getNextId());
            log.debug(user.toString());
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug(users.toString());
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        checkUserName(user);
        checkUserId(user.getId());
        if (checkUser(user)) {
            log.debug(user.toString());
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User getUser(Long id) {
        log.debug(id.toString());
        checkUserId(id);
        return users.get(id);
    }

    @ExceptionHandler
    public Map<String, String> handle(final RuntimeException e) {
        return Map.of("error", "Произошла ошибка!");
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    private Long getNextId() {
        return ++nextId;
    }

    private void checkUserId(Long id) {
        if (!users.containsKey(id)) throw new UserNotFoundException(id);
    }

    private void checkUserName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) user.setName(user.getLogin());
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
