package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface RatingMpaStorage {
    List<RatingMpa> getAllRatingMPA();

    RatingMpa getRatingMpaById(int id);

    boolean validateRatingMpaId(Film film);
}
