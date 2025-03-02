package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        try {
            if (validateUser(user)) {
                user.setId(getNextId());
                users.put(user.getId(), user);
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации: ", e);
        }
        log.info("Пользователь с id - {} создан", user.getId());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        User oldUser = users.get(newUser.getId());
        try {
            if (validateUser(newUser)) {
                // если пользователь найден и все условия соблюдены, обновляем его содержимое
                UserMapper userMapper = new UserMapper();
                userMapper.map(newUser, oldUser);

                log.info("Данные пользователя c id - {} обновлены", oldUser.getId());
                return oldUser;
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации: ", e);
        }
        log.error(String.format("Пользователь с id - %d не зарегистрирован", newUser.getId()));
        throw new NotFoundException(String.format("Пользователь с id - %d не зарегистрирован", newUser.getId()));
    }

    @Override
    public User getUserById(Integer id) {
        if (id == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(id)) {
            log.error(String.format("Пользователь с id - %d не зарегистрирован", id));
            throw new NotFoundException(String.format("Пользователь с id - %d не зарегистрирован", id));
        }
        return users.get(id);
    }

    public Boolean validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Логин не должен содержать пробелы");
            throw new ValidationException("логин не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
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
