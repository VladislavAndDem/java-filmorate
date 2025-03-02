package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм с id - {} добавлен", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            FilmMapper filmMapper = new FilmMapper();
            Film oldFilm = films.get(newFilm.getId());
            filmMapper.map(newFilm, oldFilm);

            log.info("Данные фильма c id {} обновлены", oldFilm.getId());
            return oldFilm;
        }
        log.error(String.format("Фильм с id - %d не добавлен", newFilm.getId()));
        throw new NotFoundException(String.format("Фильм с id - %d не добавлен", newFilm.getId()));
    }

    @Override
    public Film getFilmById(Integer id) {
        if (id == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (!films.containsKey(id)) {
            log.error("Фильм с id = {} не найден", id);
            throw new NotFoundException(String.format("Фильм с id - %d не добавлен", id));
        }
        log.info("Фильм с id - {} получен", id);
        return films.get(id);
    }

    // вспомогательный метод для генерации идентификатора нового фильма
    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
