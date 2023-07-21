package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private int id;
}
