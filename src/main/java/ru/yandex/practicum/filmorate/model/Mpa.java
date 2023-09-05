package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.enums.MpaRating;

@Data
public class Mpa {
    int id;
    String name;

    public Mpa() {
        this.id = 6;
        this.name = "PG";
    }

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Mpa getMpaById(int id) {
        Mpa mpa = new Mpa();
        for (MpaRating mpaRating : MpaRating.values()) {
            if (mpaRating.getId() == id) {
                mpa.setName(mpaRating.getName());
                mpa.setId(mpaRating.getId());
            }
        }
        return mpa;

    }
}
