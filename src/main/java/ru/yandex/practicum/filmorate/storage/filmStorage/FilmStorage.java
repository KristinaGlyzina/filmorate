package ru.yandex.practicum.filmorate.storage.filmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film createFilm();

    public Film updateFilm();

    public List<Film> getAllFilms();

    public int getNextId();
}
