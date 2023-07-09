package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private Map<Integer, User> userMap = new HashMap<>();
    private int nextId = 1;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) throws ValidationException {
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
        userMap.put(user.getId(), user);

        log.info("User created: {}.", user);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) throws ValidationException {
        int updatedUserId = updatedUser.getId();
        User existingUser = userMap.get(updatedUserId);

        if (existingUser == null) {
            log.error("User not found: {}.", updatedUserId);
            throw new ValidationException("User not found.");
        }

        if (updatedUser.getEmail() == null || updatedUser.getEmail().isEmpty() || !updatedUser.getEmail().contains("@")) {
            log.error("Invalid email: {}.", updatedUser.getEmail());
            throw new ValidationException("Invalid email.");
        }

        if (updatedUser.getLogin() == null ||  updatedUser.getLogin().isEmpty() || updatedUser.getLogin().contains(" ")) {
            log.error("Invalid login: {}.", updatedUser.getLogin());
            throw new ValidationException("Invalid login.");
        }

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setBirthday(updatedUser.getBirthday());

        log.info("User updated: {}.", existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>(userMap.values());
        return ResponseEntity.ok(users);
    }

    private int getNextId() {
        return nextId++;
    }
}