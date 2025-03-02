package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    public final FilmStorage filmStorage;
    public final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.filmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeTheMovie(@PathVariable int id, @PathVariable int userId) {
        filmService.likeMovie(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam int count) {
        return filmService.getPopularFilms(count);
    }


}
