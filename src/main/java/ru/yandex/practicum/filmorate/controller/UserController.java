package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @PutMapping(value = "/users")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) throws ValidationException {
        int id = updatedUser.getId();
        User existingUser = userMap.get(id);
        if (existingUser == null) {
            log.error("User not found with id: {}.", id);
            throw new ValidationException("User not found.");
        }
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setBirthday(updatedUser.getBirthday());

        log.info("User updated: {}.", existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers() throws ValidationException {
        List<User> users = new ArrayList<>(userMap.values());
        if (users == null || users.isEmpty()) {
            log.error("List of users is empty.");
            throw new ValidationException("List of users is empty.");
        }
        return ResponseEntity.ok(users);
    }

    private int getNextId() {
        return nextId++;
    }
}
