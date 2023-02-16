package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long generatorId = 0;

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.get(id) != null)
            return films.get(id);
        else throw new NoSuchElementException("Фильм с таким id не найден");
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++generatorId);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм с таким id не найден: {}", film.getId());
            throw new NoSuchElementException("Фильм с таким id не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public void removeFilm(Long id) {
        if (!films.containsKey(id)) {
            log.info("Невозможно удалить фильм! Фильм с таким id не найден: {}", id);
            throw new NoSuchElementException("Невозможно удалить фильм! Фильм с таким id не найден");
        }
        films.remove(id);
    }

    @Override
    public void addLike(Long id, Long userId) {

    }

    @Override
    public void deleteLike(Long id, Long userId) {

    }

    @Override
    public List<RatingMPA> getAllRatingMpa() {
        return null;
    }

    @Override
    public RatingMPA getRatingMpaById(int id) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Genre getGenreById(int id) {
        return null;
    }

}
