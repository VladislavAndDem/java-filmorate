package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeMovie(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);

        if (film.getLikes().contains(userId)) {
            log.debug("Невозможно поставить лайк фильму дважды");
            return;
        }
        log.info("Пользователь с id - {} поставил лайк фильму с id - {}", userId, filmId);
        film.getLikes().add(userId);
    }

    public void deleteLikeFilm(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);
        userStorage.getUserById(userId);
        if (film.getLikes().isEmpty()) {
            log.error("У фильма с id - {} нет лайков", id);
            throw new NotFoundException(String.format("У фильма с id - %d нет лайков", id));
        }
        log.info("Пользователь с id - {} удалил свой лайк у фильма с id - {}", userId, id);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = filmStorage.findAll();
        films.sort((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()));
        log.info("Получить список из {} популярных фильмов", Objects.requireNonNullElse(count, 10));
        return films.subList(0, Math.min(Objects.requireNonNullElse(count, 10), films.size()));

    }
}
