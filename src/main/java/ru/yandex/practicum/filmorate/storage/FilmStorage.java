package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();
    Film getFilmById(Long id);
    Film addFilm(Film film);
    Film updateFilm(Film film);
    void removeFilm(Long id);
}
