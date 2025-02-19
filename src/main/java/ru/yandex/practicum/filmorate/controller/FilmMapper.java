package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {
    public void map(Film source, Film target) {
        target.setName(source.getName());
        target.setDuration(source.getDuration());
        target.setReleaseDate(source.getReleaseDate());
        target.setDescription(source.getDescription());
    }
}
