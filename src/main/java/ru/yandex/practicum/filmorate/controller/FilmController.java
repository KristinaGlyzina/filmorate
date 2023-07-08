package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int nextId = 1;
    private Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping
    public ResponseEntity<?> createFilm(@RequestBody Film film) throws ValidationException {
        if (filmMap.values().stream().anyMatch(f -> f.getName().equals(film.getName()))) {
            throw new ValidationException("Film already exists in the list.");
        }

        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Film title cannot be empty.");
        }

        if (film.getDescription() == null) {
            throw new ValidationException("Film description is empty.");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Film description exceeds the maximum length.");
        }

        LocalDate releaseDateLimit = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(releaseDateLimit)) {
            throw new ValidationException("Invalid film release date.");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration should be greater than zero.");
        }

        int id = getNextId();
        film.setId(id);
        filmMap.put(id, film);

        log.info("Film created: {}.", film);

        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film updatedFilm) throws ValidationException {
        int id = updatedFilm.getId();
        Film existingFilm = filmMap.get(id);
        if (existingFilm == null) {
            log.error("Film not found: {}.", id);
            throw new ValidationException("Film not found.");
        }

        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setDuration(updatedFilm.getDuration());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());

        log.info("Film updated: {}.", existingFilm);

        return ResponseEntity.ok(existingFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() throws ValidationException {
        List<Film> films = new ArrayList<>(filmMap.values());
        if (films.isEmpty()) {
            log.error("List of films is empty.");
            throw new ValidationException("List of films is empty.");
        }
        return ResponseEntity.ok(films);
    }

    private int getNextId() {
        return nextId++;
    }
}
