package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Like {
    private Integer userId;
    private Instant timestamp;
}
