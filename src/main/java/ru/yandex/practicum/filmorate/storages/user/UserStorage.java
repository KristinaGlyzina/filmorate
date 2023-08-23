package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long userId);

    User deleteUser(Long userId);

    List<User> getAllUsers();
}
