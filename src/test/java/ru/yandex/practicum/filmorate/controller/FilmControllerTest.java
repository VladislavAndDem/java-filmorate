package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmControllerTest {

    private Validator validator;
    private FilmController filmController;
    private Film film;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        filmController = new FilmController();
        film = new Film(1, "Фильм", "Описание фильма", LocalDate.of(2025, Month.JANUARY,
                1), 100);
    }

    @Test
    public void testCreateFilmValid() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    public void testCreateFilmWithNameBlank() {
        film.setName("  ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Название фильма не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithDescriptionLength201() {
        film.setDescription("A".repeat(201));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Должны быть нарушения валидации");
        assertEquals(1, violations.size(), "Должно быть одно нарушение валидации");
        assertEquals("Описание не должно превышать 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithRealiseDateEarlier28December1895() {
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateFilmWithDurationLessZero() {
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Длительность фильма должна быть больше 0", violations.iterator().next().getMessage());
    }

    @Test
    public void testUpdateFilmNewName() {
        filmController.createFilm(film);
        film.setName("Новый фильм");

        filmController.updateFilm(film);

        List<Film> films = filmController.findAll();
        assertEquals(1, films.size());
        assertEquals("Новый фильм", films.getFirst().getName());
    }

    @Test
    public void testUpdateFilmNewDescription() {
        filmController.createFilm(film);
        film.setDescription("A".repeat(150));

        filmController.updateFilm(film);

        List<Film> films = filmController.findAll();
        assertEquals(1, films.size());
        assertEquals("A".repeat(150), films.getFirst().getDescription());
    }

    @Test
    public void testUpdateFilmNewReleaseDate() {
        filmController.createFilm(film);
        film.setReleaseDate(LocalDate.of(1900, Month.MAY, 5));

        filmController.updateFilm(film);

        List<Film> films = filmController.findAll();
        assertEquals(1, films.size());
        assertEquals(LocalDate.of(1900, Month.MAY, 5), films.getFirst().getReleaseDate());
    }

    @Test
    public void testUpdateFilmNewDuration() {
        filmController.createFilm(film);
        film.setDuration(199);

        filmController.updateFilm(film);

        List<Film> films = filmController.findAll();
        assertEquals(1, films.size());
        assertEquals(199, films.getFirst().getDuration());
    }
}