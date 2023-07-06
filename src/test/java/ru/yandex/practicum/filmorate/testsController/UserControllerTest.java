package ru.yandex.practicum.filmorate.testsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void addUser() throws ValidationException {
    User user = new User();
    user.setName("name");
    user.setLogin("login");
    user.setEmail("test@example.com");
    user.setBirthday(LocalDate.of(2001, 1, 1));
    user.setId(1);

    User addedUser = userController.createUser(user);

    assertEquals(user, addedUser);
    }

    @Test
    public void addUserEmptyEmail() {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setEmail("");
        user.setBirthday(LocalDate.of(2001, 1, 1));
        user.setId(2);

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void addUserEmailWithoutSymbol() {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setEmail("testexample.com");
        user.setBirthday(LocalDate.of(2001, 1, 1));
        user.setId(3);

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void addUserEmptyLogin() {
        User user = new User();
        user.setName("name");
        user.setLogin("");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(2001, 1, 1));
        user.setId(4);

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void addUserLoginWithBlankSpace() {
        User user = new User();
        user.setName("name");
        user.setLogin(" ");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(2001, 1, 1));
        user.setId(5);

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void addUserEmptyName() throws ValidationException {
        User user = new User();
        user.setName("");
        user.setLogin("login");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(2001, 1, 1));
        user.setId(6);

        User createdUser = userController.createUser(user);

        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    public void addUserInvalidBirthDate() throws ValidationException {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(2056, 1, 1));
        user.setId(7);

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
