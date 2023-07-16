package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.filmStorage.InMemoryFilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/postFilm")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping("/updateFilm")
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException {
        return inMemoryFilmStorage.updateFilm(updatedFilm);
    }

    @GetMapping("/getFilmById/{id}")
    public Film getFilmById(@PathVariable Integer id) throws ValidationException {
        return inMemoryFilmStorage.findFilmById(id);
    }

    @GetMapping("/getPopularFilms/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false, defaultValue = "10") int count) throws ValidationException {
        return filmService.getTopLikedFilms(count);
    }

    @PutMapping("/addLike/{filmId}/like/{userId}")
    public ResponseEntity<String> likeFilm(@PathVariable Integer filmId, @PathVariable Integer userId) throws ValidationException {
        filmService.addLike(filmId, userId);
        return ResponseEntity.ok("Like successfully added.");
    }

    @DeleteMapping("/deleteLike/{filmId}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) throws ValidationException {
        filmService.deleteLike(filmId, userId);
        return ResponseEntity.ok("Like successfully deleted.");
    }

    @GetMapping("/getAllFilms")
    public List<Film> getAllFilms() throws ValidationException {
        return inMemoryFilmStorage.getAllFilms();
    }
}
