package ru.yandex.practicum.filmorate.storages.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> userMap = new HashMap<>();
    private long nextId = 1;

    public User createUser(User user) throws ValidationException {
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

        if (user.getId() != null) {
            user.setId(user.getId());
        } else {
            Long id = getNextId();
            user.setId(id);
        }


        userMap.put(user.getId(), user);

        log.info("User created: {}.", user);

        return user;
    }

    public User updateUser(User updatedUser) throws ObjectNotFoundException {
        if (updatedUser == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        Long updatedUserId = updatedUser.getId();
        User existingUser = userMap.get(updatedUserId);

        if (existingUser == null) {
            log.error("User not found: {}.", updatedUserId);
            throw new ObjectNotFoundException("User not found.");
        }

        if (updatedUser.getEmail() == null || updatedUser.getEmail().isEmpty() || !updatedUser.getEmail().contains("@")) {
            log.error("Invalid email: {}.", updatedUser.getEmail());
            throw new ObjectNotFoundException("Invalid email.");
        }

        if (updatedUser.getLogin() == null || updatedUser.getLogin().isEmpty() || updatedUser.getLogin().contains(" ")) {
            log.error("Invalid login: {}.", updatedUser.getLogin());
            throw new ObjectNotFoundException("Invalid login.");
        }

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setName(updatedUser.getName());
        existingUser.setBirthday(updatedUser.getBirthday());

        log.info("User updated: {}.", existingUser);

        return existingUser;
    }

    public User getUserById(Long id) throws ObjectNotFoundException {
        return userMap.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("User not found."));
    }

    @Override
    public User deleteUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        User deletedUser = userMap.remove(userId);
        if (deletedUser == null) {
            throw new ObjectNotFoundException("User not found, id: " + userId);
        }
        log.info("User with id {} successfully deleted", userId);
        return deletedUser;
    }

    private Long getNextId() {
        return nextId++;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(userMap.values());
        return users;
    }
}
