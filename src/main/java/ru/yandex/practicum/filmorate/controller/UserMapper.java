package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public void map(User source, User target) {
        target.setEmail(source.getEmail());
        target.setLogin(source.getLogin());
        target.setName(source.getName());
        target.setBirthday(source.getBirthday());
    }
}
