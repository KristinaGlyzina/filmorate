package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User createUser();

    public User updateUser();

    public List<User> getAllUsers();

    public int getNextId();
}
