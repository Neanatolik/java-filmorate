package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String name;
    @NotBlank
    @NonNull
    private String login;
    @Email
    private String email;
    @NonNull
    @Past
    private LocalDate birthday;

    public User(String name, String login, String email, LocalDate birthday) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }

    public void setName(String login) {
        this.name = login;
    }
}
