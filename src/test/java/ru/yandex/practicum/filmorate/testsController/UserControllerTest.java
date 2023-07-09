package ru.yandex.practicum.filmorate.testsController;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTest {
    @Test
    public void createUser_Valid() throws ValidationException {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User addedUser = userController.createUser(user);

        assertNotNull(addedUser);
        assertEquals(user.getEmail(), addedUser.getEmail());
        assertEquals(user.getLogin(), addedUser.getLogin());
        assertEquals(user.getName(), addedUser.getName());
        assertEquals(user.getBirthday(), addedUser.getBirthday());
    }

    @Test
    public void createUser_EmptyEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_InvalidEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("invalid.email");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_EmptyLogin() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_LoginWithSpaces() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("user name");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_EmptyName() throws ValidationException {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User addedUser = userController.createUser(user);

        assertNotNull(addedUser);
        assertEquals(user.getLogin(), addedUser.getName());
    }

    @Test
    public void createUser_FutureBirthday() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

}
