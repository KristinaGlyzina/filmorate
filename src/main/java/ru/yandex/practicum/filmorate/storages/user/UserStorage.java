package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User getUserById(Long userId);

    User delete(Long userId);

    List<User> getAllUsers();
}
