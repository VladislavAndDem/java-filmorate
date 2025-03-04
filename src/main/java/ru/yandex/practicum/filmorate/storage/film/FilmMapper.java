package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {
    public void map(Film source, Film other) {

        other.setName(source.getName());
        other.setDuration(source.getDuration());
        other.setReleaseDate(source.getReleaseDate());
        other.setDescription(source.getDescription());
    }
}
