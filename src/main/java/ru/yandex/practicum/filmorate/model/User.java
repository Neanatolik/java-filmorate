package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String name;
    @NotBlank
    @NonNull
    private String login;
    @Email
    @NotBlank
    private String email;
    @NonNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public User(String name, String login, String email, LocalDate birthday) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }

    public void setName(String login) {
        this.name = login;
    }

    public void addFriend(Long l) {
        friends.add(l);
    }
}
