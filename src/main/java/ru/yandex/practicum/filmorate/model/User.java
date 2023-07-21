package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private final String login;
    private final String email;
    private final LocalDate birthday;
    private int id = 0;
    private String name;

    public void setName(String login) {
        this.name = login;
    }
}
