package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) throws ObjectNotFoundException {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user == null || friend == null) {
            throw new ObjectNotFoundException("User or friend not found.");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info(String.format("The user %s added the friend %s", user.getName(), friend.getName()));
    }

    public void deleteFriend(Integer userId, Integer friendId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user == null || friend == null) {
            throw new ValidationException("User or friend not found.");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        log.info(String.format("The user %s deleted friend %s.", user.getName(), friend.getName()));
    }

    public List<User> getFriends(int userId) throws ObjectNotFoundException {
        User user = userStorage.getUserById(userId);

        if (user == null) {
            throw new ObjectNotFoundException("User not found.");
        }

        Set<Integer> friendsIds = user.getFriends();
        List<User> friends = new ArrayList<>();

        for (Integer friendId : friendsIds) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int friendId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(friendId);

        if (user == null || otherUser == null) {
            throw new ValidationException("Invalid user ID provided.");
        }

        Set<Integer> userFriendsIds = user.getFriends();
        List<User> userFriends = new ArrayList<>();
        for (Integer id : userFriendsIds) {
            User friend = userStorage.getUserById(id);
            if (friend != null && friend.getId() != user.getId() && friend.getId() != otherUser.getId()) {
                userFriends.add(friend);
            }
        }

        Set<Integer> otherUserFriendsIds = otherUser.getFriends();
        List<User> otherUserFriends = new ArrayList<>();
        for (Integer id : otherUserFriendsIds) {
            User friend = userStorage.getUserById(id);
            if (friend != null && friend.getId() != user.getId() && friend.getId() != otherUser.getId()) {
                otherUserFriends.add(friend);
            }
        }

        List<User> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);

        return commonFriends;
    }
}
