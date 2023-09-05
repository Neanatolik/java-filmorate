package ru.yandex.practicum.filmorate.exceptions;

public class GenreNotFoundException extends RuntimeException {
    private final Long parameter;

    public GenreNotFoundException(Long parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter.toString();
    }
}
