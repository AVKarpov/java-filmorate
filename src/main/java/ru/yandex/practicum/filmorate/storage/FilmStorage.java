package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();
    Film getFilmById(Long id);
    List<Film> getPopularFilms(int count);
    Film addFilm(Film film);
    Film updateFilm(Film film);
    void removeFilm(Long id);
    void addLike(Long id, Long userId);
    void deleteLike(Long id, Long userId);
    List<RatingMpa> getAllRatingMpa();
    RatingMpa getRatingMpaById(int id);
    List<Genre> getAllGenres();
    Genre getGenreById(int id);
}
