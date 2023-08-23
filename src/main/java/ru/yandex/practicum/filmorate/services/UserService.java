package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;
    private FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        List<User> friends = friendStorage.getFriends(userId);
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<User> userFriends = friendStorage.getFriends(userId);
        List<User> friendFriends = friendStorage.getFriends(friendId);

        List<User> commonFriends = new ArrayList<>();
        for (User userFriend : userFriends) {
            if (friendFriends.contains(userFriend)) {
                commonFriends.add(userFriend);
            }
        }
        return commonFriends;
    }
}