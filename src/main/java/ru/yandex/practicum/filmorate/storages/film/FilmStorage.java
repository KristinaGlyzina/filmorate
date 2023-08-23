package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long filmId);

    Film deleteFilm(Long filmId);

    List<Film> getAllFilms();
}
