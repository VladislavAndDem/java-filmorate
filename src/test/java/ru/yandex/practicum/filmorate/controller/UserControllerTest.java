package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    private Validator validator;
    private UserController userController;
    private User user;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userController = new UserController();
        user = new User(1, "Vlad@mail.ru", "login", "vladislav",
                LocalDate.of(2000, Month.MAY, 10));
    }

    @Test
    public void testCreateUserValid() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    public void testCreateUserWithEmailNotValid() {
        user.setEmail("vlad-mail.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Электронный адресс не соответствует формату", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateUserWithLoginNotValid() {
        user.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("логин не может быть пустым и состоять из пробелов", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateUserWithNameNull() {
        user.setName(null);
        userController.create(user);
        Collection<User> collection = userController.findAll();
        assertEquals(collection.iterator().next().getName(), collection.iterator().next().getLogin());
    }

    @Test
    public void testCreateUserWithBirthdayNotValid() {
        user.setBirthday(LocalDate.of(2026, Month.MAY, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }

    @Test
    public void testUpdateUserNewEmail() {
        userController.create(user);
        user.setEmail("new@mail.ru");

        userController.updateUser(user);

        Collection<User> users = userController.findAll();
        assertEquals(1, users.size());
        assertEquals("new@mail.ru", users.iterator().next().getEmail());
    }

    @Test
    public void testUpdateUserNewLogin() {
        userController.create(user);
        user.setLogin("newLogin");

        userController.updateUser(user);

        Collection<User> users = userController.findAll();
        assertEquals(1, users.size());
        assertEquals("newLogin", users.iterator().next().getLogin());
    }

    @Test
    public void testUpdateFilmNewName() {
        userController.create(user);
        user.setName("notVlad");

        userController.updateUser(user);

        Collection<User> users = userController.findAll();
        assertEquals(1, users.size());
        assertEquals("notVlad", users.iterator().next().getName());
    }

    @Test
    public void testUpdateUserNewBirthday() {
        userController.create(user);
        user.setBirthday(LocalDate.of(1999, Month.MAY, 5));

        userController.updateUser(user);

        Collection<User> users = userController.findAll();
        assertEquals(1, users.size());
        assertEquals(LocalDate.of(1999, Month.MAY, 5), users.iterator().next().getBirthday());
    }
}