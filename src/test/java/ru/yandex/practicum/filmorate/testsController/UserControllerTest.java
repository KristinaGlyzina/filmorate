package ru.yandex.practicum.filmorate.testsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    private User user;
    private UserController userController;
    private UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
        userController = new UserController(userStorage, new UserService(userStorage, null));
        user = User.builder()
                .name("Kristina")
                .login("KristinaGl")
                .email("kg@.ru")
                .birthday(LocalDate.of(2001, 10, 21))
                .build();

    }

    @Test
    public void addUserWithValidData() {
        User createdUser = userController.createUser(user);
        assertEquals(user, createdUser);
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    public void addUserWithInvalidEmail() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, userController.getUsers().size());
    }

    @Test
    public void addUserWithLoginIsEmpty() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, userController.getUsers().size());
    }

    @Test
    public void addUserWithValidName() {
        user.setName("");
        User createdUser = userController.createUser(user);
        assertTrue(createdUser.getName().equals(user.getLogin()));
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    public void addUserWithValidBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, userController.getUsers().size());
    }
}
