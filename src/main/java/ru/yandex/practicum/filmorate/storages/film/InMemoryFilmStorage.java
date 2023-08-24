package ru.yandex.practicum.filmorate.storages.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private long nextId = 1;
    private Map<Long, Film> filmMap = new HashMap<>();

    public Film create(Film film) throws ValidationException {

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

        Long id = getNextId();
        film.setId(id);

        filmMap.put(id, film);

        log.info("Film created: {}.", film);

        return film;
    }

    public Film update(Film updatedFilm) throws ValidationException, ObjectNotFoundException {

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

        Long updatedFilmId = updatedFilm.getId();
        Film existingFilm = filmMap.get(updatedFilmId);

        if (existingFilm == null) {
            log.error("Film not found: {}.", updatedFilmId);
            throw new ObjectNotFoundException("Film not found.");
        }
        existingFilm.setId(updatedFilm.getId());
        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setDuration(updatedFilm.getDuration());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());

        filmMap.put(existingFilm.getId(), existingFilm);

        log.info("Film updated: {}.", existingFilm);

        return existingFilm;
    }

    public Film getFilmById(Long id) throws ObjectNotFoundException {
        return filmMap.values().stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Film not found."));
    }

    public Long getNextId() {
        return nextId++;
    }

    public Film delete(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Film id cannot be null");
        }
        Film deletedFilm = filmMap.remove(filmId);
        if (deletedFilm == null) {
            throw new ObjectNotFoundException("Film not found, id: " + filmId);
        }
        log.info("Film deleted: {}", deletedFilm);

        return deletedFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>(filmMap.values());
        return films;
    }
}
