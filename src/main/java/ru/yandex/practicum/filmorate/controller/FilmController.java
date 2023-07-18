package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
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

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException, ObjectNotFoundException {
        return inMemoryFilmStorage.updateFilm(updatedFilm);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) throws ObjectNotFoundException {
        return inMemoryFilmStorage.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false, defaultValue = "10") int count) throws ValidationException {
        return filmService.getTopLikedFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<String> likeFilm(@PathVariable Integer filmId, @PathVariable Integer userId) throws ValidationException {
        filmService.addLike(filmId, userId);
        return ResponseEntity.ok("Like successfully added.");
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) throws ValidationException {
        filmService.deleteLike(filmId, userId);
        return ResponseEntity.ok("Like successfully deleted.");
    }

    @GetMapping
    public List<Film> getAllFilms() throws ObjectNotFoundException {
        return inMemoryFilmStorage.getAllFilms();
    }
}
