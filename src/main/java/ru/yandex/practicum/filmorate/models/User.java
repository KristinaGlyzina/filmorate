package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

    public User(Long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;
        if (friends == null) {
            this.friends = new HashSet<>();
        }
    }

    public Map<String, Object> userToMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("login", login);
        userMap.put("name", name);
        userMap.put("birthday", birthday);
        return userMap;
    }
}
