package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmorateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        checkUserName(user);
        if (checkUser(user)) {
            user.setId(getNextId());
            log.debug(user.toString());
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        checkUserName(user);
        if (users.containsKey(user.getId())) {
            log.debug(user.toString());
            users.put(user.getId(), user);
        } else throw new FilmorateException("Нет такого id");
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    private boolean checkUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new FilmorateException("Дата рождения не может быть в будущем");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new FilmorateException("Электронная почта не может быть пустой");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new FilmorateException("Логин не может быть пустым");
        } else return true;
    }

    private void checkUserName(User user) {
        if (Objects.isNull(user.getName())) user.setName(user.getLogin());
    }

    private Integer getNextId() {
        return ++id;
    }

}
