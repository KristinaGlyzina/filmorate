package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private int nextId = 1;
    private Map<Integer, User> userMap = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) throws ValidationException {
        validateUser(user);

        if (userMap.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("User with the same email already exists.");
        }

        int id = getNextId();
        user.setId(id);
        userMap.put(id, user);

        log.info("User created: {}.", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) throws ValidationException {
        int id = updatedUser.getId();
        User existingUser = userMap.get(id);
        if (existingUser == null) {
            log.error("User not found: {}.", id);
            throw new ValidationException("User not found.");
        }

        validateUser(updatedUser);

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setBirthday(updatedUser.getBirthday());

        log.info("User updated: {}.", existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() throws ValidationException {
        List<User> users = new ArrayList<>(userMap.values());
        if (users.isEmpty()) {
            log.error("List of users is empty.");
            throw new ValidationException("List of users is empty.");
        }
        return ResponseEntity.ok(users);
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Invalid email format.");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Invalid login format.");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday() != null && user.getBirthday().isAfter(currentDate)) {
            throw new ValidationException("Invalid birthday date.");
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
