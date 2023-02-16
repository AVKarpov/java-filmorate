package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping(value = "/films/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    //DELETE /films/{id}
    @DeleteMapping(value = "/films/{id}")
    public void delete(@PathVariable Long id) {
        filmService.removeFilm(id);
    }

    //PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму
    @PutMapping(value = "/films/{id}/like/{userId}")
    public void setLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    //DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк
    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    //GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, возвращаются первые 10.
    @GetMapping(value = "/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    //GET /genres
    @GetMapping(value = "/genres")
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    //GET /genres/{id}
    @GetMapping(value = "/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }

    //GET /mpa
    @GetMapping(value = "/mpa")
    public List<RatingMPA> getAllRatingMpa() {
        return filmService.getAllRatingMpa();
    }

    //GET /mpa/{id}
    @GetMapping(value = "/mpa/{id}")
    public RatingMPA getRatingMpaById(@PathVariable int id) {
        return filmService.getRatingMpaById(id);
    }
}
