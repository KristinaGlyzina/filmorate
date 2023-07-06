package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private List<User> userList = new ArrayList<>();

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        if (userList.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("User with email already exists.");
        }

        if (userList.stream().anyMatch(u -> u.getLogin().equals(user.getLogin()))) {
            throw new ValidationException("User with login already exists.");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Invalid email.");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Invalid login.");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday() != null && user.getBirthday().isAfter(currentDate)) {
            throw new ValidationException("Invalid birth date.");
        }

        userList.add(user);

        return user;
    }

    @PutMapping(value = "/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) throws ValidationException {
        User existingUser = userList.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("User not found"));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setLogin(updatedUser.getLogin());

        return existingUser;
    }

    @GetMapping(value = "users")
    public List<User> getAllUsers() throws ValidationException {
        if (userList == null || userList.isEmpty()) {
            throw new ValidationException("List of users is empty");
        }

        return userList;
    }
}
