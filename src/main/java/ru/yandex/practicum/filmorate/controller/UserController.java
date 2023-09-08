package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserDbStorage userStorage) {
        this.userService = new UserService(userStorage);
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST /users");
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.info("PUT /users");
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void putFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("PUT /users/" + id + "/friends/" + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("DELETE /users/" + id + "/friends/" + friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("GET /users");
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("GET /users/" + id);
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("GET /users/" + id + "/friends");
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommon(@PathVariable Long otherId, @PathVariable Long id) {
        log.info("GET /users/" + id + "/friends/common/" + otherId);
        return userService.getCommon(otherId, id);
    }

}
