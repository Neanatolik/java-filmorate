package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmorateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static  final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        if (user.getName().isBlank()) user.setName(user.getLogin());
        if (checkUser(user)) {
            log.debug(user.toString());
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping("/users")
    public void updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.debug(user.toString());
            users.put(user.getId(), user);
            System.out.println(user);
        }
    }

    @GetMapping("/users")
    public Map<Integer, User> getUsers() {
        return users;
    }

    private boolean checkUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new FilmorateException("Дата рождения не может быть в будущем");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new FilmorateException("Электронная почта не может быть пустой");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new FilmorateException("Длительность фильма должна быть положительной");
        } else return true;
    }

}
