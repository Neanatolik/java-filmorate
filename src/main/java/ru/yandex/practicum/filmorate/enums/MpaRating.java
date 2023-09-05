package ru.yandex.practicum.filmorate.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MpaRating {

    G(1, "G"),
    PG(2, "PG"),
    PG13(3, "PG-13"),
    R(4, "R"),
    NC17(5, "NC-17");
    int id;
    String name;

    MpaRating(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<MpaRating> getNames() {
        List<MpaRating> list = new ArrayList<>();
        for (MpaRating mpaRating : MpaRating.values())
            if (!mpaRating.name.equals("Нет MPA"))
                list.add(mpaRating);
        return list;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}

