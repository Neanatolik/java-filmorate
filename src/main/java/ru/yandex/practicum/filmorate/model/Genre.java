package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.enums.Genres;

@Data
public class Genre {
    private int id;
    private String name;

    public Genre() {
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Genre getGenreById(int id) {
        Genre genre = new Genre();
        for (Genres genres : Genres.values()) {
            if (genres.getId() == id) {
                genre.setName(genres.getName());
                genre.setId(genres.getId());
            }
        }
        return genre;
    }
}
