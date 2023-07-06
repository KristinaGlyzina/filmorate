package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*
Денис, привет!)
у меня есть вопрос - в файле pom.xml у меня подсвечивается
<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId> вот эта строка красным, у меня не получается это исправить
			</plugin>
		</plugins>
	</build>
	подскажи пожалуйста, как это можно исправить. ошибка: Plugin 'org.springframework.boot:spring-boot-maven-plugin:' not found

Ну и конечно жду твоих комментариев и рекомендаций по исправлению моего кода ;)
 */
@RestController
@Slf4j
public class FilmController {
    private List<Film> filmList = new ArrayList<>();


    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (filmList.stream().anyMatch(f -> f.getName().equals(film.getName()))) {
        throw new ValidationException("Film already exists in list.");
        }

        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Film title cannot be empty.");
        }

        if (film.getDescription() == null) {
            throw new ValidationException("Film description is empty.");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Film description maximum length exceeded.");
        }

        LocalDate releaseDateLimit = LocalDate.of(1985, 12, 28);
        if (film.getReleaseDate().isBefore(releaseDateLimit)) {
            throw new ValidationException("Invalid film release date");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration should be more, than '0'.");
        }
        filmList.add(film);

        return film;
    }

    @PutMapping(value = "/films/{id}")
    public Film updateFilm(@PathVariable int id, @RequestBody Film updatedFilm) throws ValidationException {
        Film existingFilm = filmList.stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("Film not found."));

        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setDuration(updatedFilm.getDuration());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());

        return existingFilm;
    }

    @GetMapping(value = "/films")
    public List<Film> getAllFilms() throws ValidationException {
        if (filmList == null || filmList.isEmpty()) {
            throw new ValidationException("List of films is empty.");
        }
        return filmList;
    }

}
