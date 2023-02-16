package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895,12,28);

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        validate(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        return filmStorage.updateFilm(film);
    }

    public void removeFilm(Long id) {
        filmStorage.removeFilm(id);
    }

    //добавление лайка
    public void addLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.info("Добавление лайка. Фильм с id = {} не найден", id);
            throw new NoSuchElementException(String.format("Фильм с id = %d не найден", id));
        }
        if (userStorage.getUserById(userId) == null) {
            log.info("Добавление лайка. Пользователь с id = {} не найден", userId);
            throw new NoSuchElementException(String.format("Пользователь с id = %d не найден", userId));
        }

        filmStorage.addLike(id, userId);
    }

    // удаление лайка
    public void removeLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.info("Удаление лайка. Фильм с id = {} не найден", id);
            throw new NoSuchElementException(String.format("Фильм с id = %d не найден", id));
        }
        if (userStorage.getUserById(userId) == null) {
            log.info("Удаление лайка. Пользователь с id = {} не найден", userId);
            throw new NoSuchElementException(String.format("Пользователь с id = %d не найден", userId));
        }

        filmStorage.deleteLike(id, userId);
    }

    // вывод наиболее популярных фильмов по количеству лайков
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public List<RatingMPA> getAllRatingMpa() {
        return filmStorage.getAllRatingMpa();
    }

    public RatingMPA getRatingMpaById(int id) {
       return filmStorage.getRatingMpaById(id);
    }

    private void validate(Film film) {
        //название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Название фильма пустое: {}", film);
            throw new ValidationException("Название фильма не может быть пустым");
        }
        //максимальная длина описания — 200 символов
        if (film.getDescription().length() > 200) {
            log.info("Превышена максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания не должна превышать 200 символов");
        }
        //продолжительность фильма должна быть положительной
        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма меньше 0");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        //дата релиза — не раньше 28 декабря 1895 года
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            log.info("Указана некорректная дата релиза фильма: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не корректная");
        }
    }

}
