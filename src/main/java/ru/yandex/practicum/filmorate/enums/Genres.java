package ru.yandex.practicum.filmorate.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genres {
    Comedy("Комедия", 1),
    Drama("Драма", 2),
    Cartoon("Мультфильм", 3),
    Thriller("Триллер", 4),
    Documentary("Документальный", 5),
    ActionMovie("Боевик", 6),
    NoGenre("Нет Жанра", 7);
    private String name;
    private int id;

    Genres(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static List<Genres> getNames() {
        List<Genres> list = new ArrayList<>();
        for (Genres g : Genres.values())
            if (!g.name.equals("Нет Жанра"))
                list.add(g);
        return list;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
