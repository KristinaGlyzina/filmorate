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

    @PostMapping(value = "/films")
    public ResponseEntity<?> createFilm(@RequestBody Film film) throws ValidationException {

        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Film title cannot be empty.");
            throw new ValidationException("Film title cannot be empty.");
        }

        if (film.getDescription().length() > 200) {
            log.error("Film description exceeds the maximum length.");
            throw new ValidationException("Film description exceeds the maximum length.");
        }

        LocalDate releaseDateLimit = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(releaseDateLimit)) {
            log.error("Invalid film release date.");
            throw new ValidationException("Invalid film release date.");
        }

        if (film.getDuration() <= 0) {
            log.error("Film duration should be greater than zero.");
            throw new ValidationException("Film duration should be greater than zero.");
        }

        int id = getNextId();
        film.setId(id);

        filmMap.put(id, film);

        log.info("Film created: {}.", film);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/films/{id}")
    public ResponseEntity<?> updateFilm(@PathVariable int id, @RequestBody Film updatedFilm) throws ValidationException {

        if (updatedFilm.getName() == null || updatedFilm.getName().isEmpty()) {
            log.error("Film title cannot be empty.");
            throw new ValidationException("Film title cannot be empty.");
        }

        LocalDate releaseDateLimit = LocalDate.of(1895, 12, 28);
        if (updatedFilm.getReleaseDate().isBefore(releaseDateLimit)) {
            log.error("Invalid film release date.");
            throw new ValidationException("Invalid film release date.");
        }

        if (updatedFilm.getDescription().length() > 200) {
            log.error("Film description exceeds the maximum length.");
            throw new ValidationException("Film description exceeds the maximum length.");
        }

        if (updatedFilm.getDuration() <= 0) {
            log.error("Film duration should be greater than zero.");
            throw new ValidationException("Film duration should be greater than zero.");
        }

        Film existingFilm = filmMap.get(id);
        if (existingFilm == null) {
            log.error("Film not found: {}.", id);
            throw new ValidationException("Film not found.");
        }
        existingFilm.setId(updatedFilm.getId());
        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setDuration(updatedFilm.getDuration());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());

        filmMap.put(existingFilm.getId(), existingFilm);

        log.info("Film updated: {}.", existingFilm);

        return ResponseEntity.ok(existingFilm);
    }

    @GetMapping(value = "/clients")
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
