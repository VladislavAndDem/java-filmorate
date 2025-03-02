package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public void map(User first, User second) {
        if (second == null) {
            throw new NotFoundException("target is null");
        }
        second.setEmail(first.getEmail());
        second.setLogin(first.getLogin());
        second.setName(first.getName());
        second.setBirthday(first.getBirthday());
    }
}
