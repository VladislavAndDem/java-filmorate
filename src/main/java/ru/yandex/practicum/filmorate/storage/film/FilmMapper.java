package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {
    public void map(Film first, Film second) {

        second.setName(first.getName());
        second.setDuration(first.getDuration());
        second.setReleaseDate(first.getReleaseDate());
        second.setDescription(first.getDescription());
    }
}
