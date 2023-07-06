package ru.yandex.practicum.filmorate.testsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void addFilm() throws ValidationException {
    Film film = new Film();
    film.setName("name");
    film.setDescription("description");
    film.setId(1);
    film.setDuration(120);
    film.setReleaseDate(LocalDate.of(2020, 1, 1));

    Film addedFilm = filmController.addFilm(film);

    assertEquals(film, addedFilm);
    }

    @Test
    public void addFilmEmptyName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("film description");
        film.setId(2);
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addFilmLongDescription() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("Введение в анализ данных — процесс извлечения, очистки, преобразования и моделирования данных для получения полезной информации. Он включает в себя широкий спектр методов и инструментов, таких как статистический анализ, машинное обучение, визуализация данных и многое другое. Анализ данных используется во многих областях, включая бизнес, науку, маркетинг, финансы и здравоохранение, для принятия информированных решений и выявления скрытых закономерностей и трендов. Он играет важную роль в современном мире, где данные становятся все более объемными и сложными. Владение навыками анализа данных может открыть двери к множеству возможностей и улучшить карьерные перспективы.");
        film.setId(3);
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addFilmInvalidReleaseDate() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("Введение в анализ данных — процесс извлечения, очистки, преобразования и моделирования данных для получения полезной информации. Он включает в себя широкий спектр методов и инструментов, таких как статистический анализ, машинное обучение, визуализация данных и многое другое. Анализ данных используется во многих областях, включая бизнес, науку, маркетинг, финансы и здравоохранение, для принятия информированных решений и выявления скрытых закономерностей и трендов. Он играет важную роль в современном мире, где данные становятся все более объемными и сложными. Владение навыками анализа данных может открыть двери к множеству возможностей и улучшить карьерные перспективы.");
        film.setId(4);
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1576, 1, 1));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addFilmInvalidDuration() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("Введение в анализ данных — процесс извлечения, очистки, преобразования и моделирования данных для получения полезной информации. Он включает в себя широкий спектр методов и инструментов, таких как статистический анализ, машинное обучение, визуализация данных и многое другое. Анализ данных используется во многих областях, включая бизнес, науку, маркетинг, финансы и здравоохранение, для принятия информированных решений и выявления скрытых закономерностей и трендов. Он играет важную роль в современном мире, где данные становятся все более объемными и сложными. Владение навыками анализа данных может открыть двери к множеству возможностей и улучшить карьерные перспективы.");
        film.setId(4);
        film.setDuration(0);
        film.setReleaseDate(LocalDate.of(1576, 1, 1));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }
}
