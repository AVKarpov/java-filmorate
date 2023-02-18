package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Genre getGenreById(int id);

    List<Genre> getFilmGenres(long filmId);

    void addGenresToFilm(long filmId, List<Genre> genres);

    void updateGenresAtFilm(long filmId, List<Genre> genres);

    void deleteGenresFromFilm(long film_id);
}
