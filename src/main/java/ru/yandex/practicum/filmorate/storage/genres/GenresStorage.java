package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenresStorage {
    Genres getGenre(int id);

    List<Genres> getGenres();

    Long amountOfGenres();
}
