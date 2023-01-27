package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
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
        film.getLikes().add(id);
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
        film.getLikes().remove(userId);
    }

    // вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getPopularFilms(Integer count) {
        log.info("Получение списка популярных фильмов count = {}", count);
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
