package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film delete(Long filmId);

    Film getFilmById(Long filmId);

    List<Film> getAllFilms();
}
