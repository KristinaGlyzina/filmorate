package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storages.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;
    private final UserService userService;
    private final FriendStorage friendStorage;
    private User user;
    private User anotherUser;
    private User commonFriend;
    private Film film;
    private Film anotherFilm;

    @BeforeEach
    public void setUp() {

        user = User.builder()
                .name("Ivan")
                .login("grandMaster")
                .email("ivan.ivanov@mail.ru")
                .birthday(LocalDate.of(2003, 02, 15))
                .build();

        anotherUser = User.builder()
                .name("Vova")
                .login("puma")
                .email("vova.petrov@mail.ru")
                .birthday(LocalDate.of(2001, 05, 01))
                .build();

        commonFriend = User.builder()
                .name("Lola")
                .login("lol")
                .email("lolaPick@mail.ru")
                .birthday(LocalDate.of(2004, 04, 02))
                .build();

        film = Film.builder()
                .name("New Film")
                .description("This is a description of the new test film. It has a maximum length of 200 symbols.")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(120)
                .build();

        film.setMpa(new Mpa(3, "PG-13"));
        film.setLikes(new HashSet<>());

        film.setGenres(new HashSet<>(Arrays.asList(
                new Genre(1, "Комедия"),
                new Genre(3, "Мультфильм")
        )));

        anotherFilm = Film.builder()
                .name("New Another Film")
                .description("This is a description of the new test another film.")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(60)
                .build();

        anotherFilm.setMpa(new Mpa(3, "PG-13"));
        anotherFilm.setLikes(new HashSet<>());

        anotherFilm.setGenres(new HashSet<>(Arrays.asList(
                new Genre(1, "Комедия")
        )));
    }

    @Test
    public void checkCreateAndGetUserById() {

        user = userStorage.create(user);

        Long userId = user.getId();

        User retrievedUser = userStorage.getUserById(userId);

        assertThat(retrievedUser)
                .isNotNull()
                .extracting(User::getId)
                .isEqualTo(userId);
    }

    @Test
    public void updateUserTest() {
        user = userStorage.create(user);

        user.setName("Petr");
        user.setEmail("petr@mail.ru");

        user = userStorage.update(user);

        User updatedUser = userStorage.getUserById(user.getId());

        assertThat(updatedUser)
                .isNotNull()
                .matches(u -> u.getName().equals("Petr"))
                .matches(u -> u.getEmail().equals("petr@mail.ru"));
    }

    @Test
    public void createFilmTest() {
        film = filmStorage.create(film);

        assertThat(film.getId())
                .isNotNull();
    }

    @Test
    public void updateFilmTest() {
        film = filmStorage.create(film);

        film.setName("New name");
        film = filmStorage.update(film);

        Film updatedFilm = filmStorage.getFilmById(film.getId());

        assertThat(updatedFilm.getName())
                .isEqualTo("New name");
    }

    @Test
    public void getFilmByIdTest() {
        film = filmStorage.create(film);

        Film gotFilm = filmStorage.getFilmById(film.getId());

        assertThat(gotFilm)
                .isNotNull()
                .isEqualToComparingFieldByField(film);
    }

    @Test
    public void deleteFilmTest() {
        film = filmStorage.create(film);

        Film deletedFilm = film;

        filmStorage.delete(film.getId());

        assertThat(deletedFilm)
                .isNotNull();

        assertThatThrownBy(() -> filmStorage.getFilmById(deletedFilm.getId()))
                .isInstanceOf(ObjectNotFoundException.class);

    }

    @Test
    void likeFilmTest() {
        user = userStorage.create(user);
        film = filmStorage.create(film);

        filmService.likeFilm(film.getId(), user.getId());

        Film likedFilm = filmStorage.getFilmById(film.getId());
        assertThat(likedFilm.getLikes())
                .contains(user.getId());
    }

    @Test
    void deleteLikeTest() {
        user = userStorage.create(user);
        film = filmStorage.create(film);

        filmService.likeFilm(film.getId(), user.getId());

        filmService.deleteLike(film.getId(), user.getId());

        Film unlikedFilm = filmStorage.getFilmById(film.getId());
        assertThat(unlikedFilm.getLikes())
                .doesNotContain(user.getId());
    }

    @Test
    void getPopularFilmsTest() {
        user = userStorage.create(user);
        anotherUser = userStorage.create(anotherUser);

        film = filmStorage.create(film);
        anotherFilm = filmStorage.create(anotherFilm);

        filmService.likeFilm(film.getId(), user.getId());
        filmService.likeFilm(film.getId(), anotherUser.getId());

        filmService.likeFilm(anotherFilm.getId(), user.getId());

        List<Film> popular = filmService.getPopularFilms(1);
        assertThat(popular)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void addFriendTest() {

        user = userStorage.create(user);
        anotherUser = userStorage.create(anotherUser);

        userService.addFriend(user.getId(), anotherUser.getId());

        assertThat(friendStorage.getFriends(user.getId()))
                .contains(anotherUser);
    }

    @Test
    void deleteFriendTest() {
        user = userStorage.create(user);
        anotherUser = userStorage.create(anotherUser);

        userService.addFriend(user.getId(), anotherUser.getId());
        userService.deleteFriend(user.getId(), anotherUser.getId());

        assertThat(friendStorage.getFriends(user.getId()))
                .doesNotContain(anotherUser);
    }

    @Test
    void getCommonFriendsTest() {
        user = userStorage.create(user);
        anotherUser = userStorage.create(anotherUser);
        commonFriend = userStorage.create(commonFriend);

        userService.addFriend(user.getId(), commonFriend.getId());
        userService.addFriend(anotherUser.getId(), commonFriend.getId());

        assertThat(userService.getCommonFriends(user.getId(), anotherUser.getId()))
                .containsOnly(commonFriend);
    }
}