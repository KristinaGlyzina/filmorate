package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.filmStorage.InMemoryFilmStorage;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer filmId, Integer userId) throws ValidationException {
        Film film = filmStorage.getFilmById(filmId);

        if (hasLiked(film, userId)) {
            throw new ValidationException("User has already liked the film.");
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setTimestamp(Instant.now());

        film.getLikes().add(like);
        log.info("Like added.");
    }

    public void deleteLike(Integer filmId, Integer userId) throws ObjectNotFoundException {
        Film film = filmStorage.getFilmById(filmId);

        Like likeToDelete = null;
        for (Like like : film.getLikes()) {
            if (like.getUserId().equals(userId)) {
                likeToDelete = like;
                break;
            }
        }

        if (likeToDelete == null) {
            throw new ObjectNotFoundException("The user has not liked this film.");
        }
        film.getLikes().remove(likeToDelete);
        log.info("The like deleted.");
    }

    public List<Film> getTopLikedFilms(int count) throws ValidationException {
        List<Film> sortedFilms = new ArrayList<>(filmStorage.getAllFilms());
        sortedFilms.sort(Comparator.comparingInt(film -> film.getLikes().size()));
        Collections.reverse(sortedFilms);
        return sortedFilms;
    }

    public boolean hasLiked(Film film, Integer userId) {
        for (Like like : film.getLikes()) {
            if (like.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}
