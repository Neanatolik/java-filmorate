package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundUserException(final UserNotFoundException e) {
        return new ErrorResponse(String.format("Пользователь с id \"%s\" не найден.", e.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundFilmException(final FilmNotFoundException e) {
        return new ErrorResponse(String.format("Фильм с id \"%s\" не найден.", e.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundGenreException(final GenreNotFoundException e) {
        return new ErrorResponse(String.format("Genre с id \"%s\" не найден.", e.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundMpaException(final MpaNotFoundException e) {
        return new ErrorResponse(String.format("Mpa с id \"%s\" не найден.", e.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectFilmException(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

}
