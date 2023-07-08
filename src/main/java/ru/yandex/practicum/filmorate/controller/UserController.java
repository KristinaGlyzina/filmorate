package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> userMap = new HashMap<>();
    private int nextId = 1;

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        if (userMap.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("User with email already exists.");
        }

        if (userMap.values().stream().anyMatch(u -> u.getLogin().equals(user.getLogin()))) {
            throw new ValidationException("User with login already exists.");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Invalid email: {}.", user.getEmail());
            throw new ValidationException("Invalid email.");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Invalid login: {}.", user.getLogin());
            throw new ValidationException("Invalid login.");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday() != null && user.getBirthday().isAfter(currentDate)) {
            log.error("Invalid birth date: {}.", user.getBirthday());
            throw new ValidationException("Invalid birth date.");
        }

        int id = getNextId();
        user.setId(id);
        userMap.put(id, user);

        log.info("User created: {}.", user);

        return user;
    }

    @PutMapping(value = "/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) throws ValidationException {
        User existingUser = userMap.values().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("User not found with id: {}.", id);
                    return new ValidationException("User not found.");
                });
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setLogin(updatedUser.getLogin());

        log.info("User updated: {}.", existingUser);

        return existingUser;
    }

    @GetMapping(value = "users")
    public Map<Integer, User> getAllUsers() throws ValidationException {
        if (userMap == null || userMap.isEmpty()) {
            log.error("List of users is empty.");
            throw new ValidationException("List of users is empty.");
        }
        return userMap;
    }

    private int getNextId() {
        return nextId++;
    }
}
