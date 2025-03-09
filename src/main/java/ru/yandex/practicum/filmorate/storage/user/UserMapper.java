package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public void map(User source, User other) {
        if (other == null) {
            throw new NotFoundException("target is null");
        }
        other.setEmail(source.getEmail());
        other.setLogin(source.getLogin());
        other.setName(source.getName());
        other.setBirthday(source.getBirthday());
    }
}
