package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) throws ValidationException {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        Friend friendUser = new Friend();
        friendUser.setName(user.getName());
        friendUser.setId(user.getId());

        Friend friendFriend = new Friend();
        friendFriend.setName(friend.getName());
        friendFriend.setId(friend.getId());

        user.getFriends().add(friendFriend);
        friend.getFriends().add(friendUser);

        log.info(String.format("The user %s added the friend %s", user.getName(), friend.getName()));
    }

    public void deleteFriend(int userId, int friendId) throws ValidationException {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        Friend friendToDelete = null;
        for (Friend f : user.getFriends()) {
            if (f.getId() == friendId) {
                friendToDelete = f;
                break;
            }
        }

        if (friendToDelete != null) {
            user.getFriends().remove(friendToDelete);
        }

        Friend userToDelete = null;
        for (Friend f : friend.getFriends()) {
            if (f.getId() == userId) {
                userToDelete = f;
                break;
            }
        }

        if (userToDelete != null) {
            friend.getFriends().remove(userToDelete);
        }

        log.info(String.format("User %s deleted friend %s.", user.getName(), friend.getName()));
    }


    public List<Friend> getCommonFriends(int userId, int friendId) throws ValidationException {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .collect(Collectors.toList());
    }
}
