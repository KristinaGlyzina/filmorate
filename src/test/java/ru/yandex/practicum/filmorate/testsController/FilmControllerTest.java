package ru.yandex.practicum.filmorate.testsController;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilmControllerTest {

    @Test
    public void addFilm_Valid() throws ValidationException {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        Film addedFilm = filmController.createFilm(film);

        assertNotNull(addedFilm);
        assertEquals(film.getName(), addedFilm.getName());
        assertEquals(film.getDescription(), addedFilm.getDescription());
        assertEquals(film.getReleaseDate(), addedFilm.getReleaseDate());
        assertEquals(film.getDuration(), addedFilm.getDuration());
    }

    @Test
    public void addFilm_EmptyName() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void addFilm_LongDescription() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("name");
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void addFilm_NegativeDuration() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(-120);

        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }
}