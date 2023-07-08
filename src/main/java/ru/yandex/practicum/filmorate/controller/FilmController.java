package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private int nextId;
    private Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (filmMap.values().stream().anyMatch(f -> f.getName().equals(film.getName()))) {
        throw new ValidationException("Film already exists in list.");
        }

        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Film title cannot be empty.");
            throw new ValidationException("Film title cannot be empty.");
        }

        if (film.getDescription() == null) {
            log.error("Film description is empty.");
            throw new ValidationException("Film description is empty.");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Film description maximum length exceeded.");
            throw new ValidationException("Film description maximum length exceeded.");
        }

        LocalDate releaseDateLimit = LocalDate.of(1985, 12, 28);
        if (film.getReleaseDate().isBefore(releaseDateLimit)) {
            log.error("Invalid film release date: {}.", film.getReleaseDate());
            throw new ValidationException("Invalid film release date.");
        }

        if (film.getDuration() <= 0) {
            log.error("Film duration should be more, than '0'.");
            throw new ValidationException("Film duration should be more, than '0'.");
        }

        int id = getNextId();
        film.setId(id);
        filmMap.put(id, film);

        return film;
    }

    @PutMapping(value = "/films/{id}")
    public Film updateFilm(@PathVariable int id, @RequestBody Film updatedFilm) throws ValidationException {
        Film existingFilm = filmMap.values().stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Film not found.");
                    return new ValidationException("Film not found.");
                });

        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setDuration(updatedFilm.getDuration());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());

        return existingFilm;
    }

    @GetMapping(value = "/films")
    public Map<Integer, Film> getAllFilms() throws ValidationException {
        if (filmMap == null || filmMap.isEmpty()) {
            log.error("List of films is empty.");
            throw new ValidationException("List of films is empty.");
        }
        return filmMap;
    }

    private int getNextId() {
        return nextId++;
    }
}
