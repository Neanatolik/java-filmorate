package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getMpas() {
        return mpaStorage.getMpas();
    }

    public Mpa getMpaById(int id) {
        checkMpa(id);
        log.debug(Integer.toString(id));
        return mpaStorage.getMpa(id);
    }

    private void checkMpa(int id) {
        if (mpaStorage.amountOfMpas() < id) throw new MpaNotFoundException(id);
    }

}
