package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class LikeComparator implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {
        return Integer.compare(film2.getAmountOfLikes(), film1.getAmountOfLikes());
    }
}
