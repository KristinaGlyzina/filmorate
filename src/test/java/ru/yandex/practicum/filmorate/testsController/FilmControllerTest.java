package ru.yandex.practicum.filmorate.testsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.like.LikeStorage;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {
    private Film film;
    private FilmController filmController;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private LikeStorage likeStorage;

    @BeforeEach
    public void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmController = new FilmController(filmStorage, new FilmService(filmStorage, userStorage,  null));
        film = Film.builder()
                .name("Pulp Fiction")
                .description("American crime film written and directed by Quentin Tarantino, " +
                        "who conceived it with Roger Avary.")
                .releaseDate(LocalDate.of(1994, 10, 14))
                .duration(154)
                .build();
    }

    @Test
    public void addFilmWithInvalidName() {
        film.setName("");

        assertThrows(ValidationException.class, () -> filmController.create(film));

        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    public void addFilmWithZeroDuration() {
        film.setDuration(0);

        assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    public void addFilmWithNegativeDuration() {
        film.setDuration(-1);

        assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals(0, filmController.getFilms().size());
    }
}