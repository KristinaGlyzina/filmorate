package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.userStorage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @PostMapping("/postUser")
    public User createUser(@RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.createUser(user);
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User updatedUser) throws ValidationException {
        return inMemoryUserStorage.updateUser(updatedUser);
    }

    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Integer id) throws ValidationException {
        return inMemoryUserStorage.findUserById(id);
    }

    @PutMapping("/addFriend/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        userService.addFriend(id, friendId);
        return ResponseEntity.ok("Friend successfully added.");
    }

    @DeleteMapping("/deleteFriend/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        userService.deleteFriend(id, friendId);
        return ResponseEntity.ok("Friend successfully deleted.");
    }

    @GetMapping("/getCommonFriends/{id}/friends/{friendId}")
    public List<Friend> getCommonFriends(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        return userService.getCommonFriends(id, friendId);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }
}