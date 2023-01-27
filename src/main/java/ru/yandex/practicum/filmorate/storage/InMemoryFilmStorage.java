package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895,12,28);
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
    public Film addFilm(Film film) {
        validate(film);
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
        validate(film);
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

    public void validate(Film film) {
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
