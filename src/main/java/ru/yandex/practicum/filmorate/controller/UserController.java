package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        try {
            if (validateUser(user)) {
                user.setId(getNextId());
                users.put(user.getId(), user);
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации: ", e);
        }
        log.debug("Пользователь создан");
        return user;
    }

    public Boolean validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        User oldUser = users.get(newUser.getId());
        try {
            if (validateUser(newUser)) {
                // если пользователь найден и все условия соблюдены, обновляем его содержимое
                UserMapper userMapper = new UserMapper();
                userMapper.map(newUser, oldUser);

                log.debug("Данные пользователя изменены");
                return oldUser;
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации: ", e);
        }
        log.error("Пользователь не найден");
        throw new ValidationException("пользователь с id = " + newUser.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
