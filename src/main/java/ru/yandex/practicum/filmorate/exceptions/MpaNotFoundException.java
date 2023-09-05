package ru.yandex.practicum.filmorate.exceptions;

public class MpaNotFoundException extends RuntimeException {
    private final Long parameter;

    public MpaNotFoundException(Long parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter.toString();
    }
}
