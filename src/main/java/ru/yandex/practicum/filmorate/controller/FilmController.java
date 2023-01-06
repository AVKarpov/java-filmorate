package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895,12,28);
    private int generatorId = 0;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(++generatorId);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм с таким id не найден: {}", film.getId());
            throw new ValidationException("Фильм с таким id не найден");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);

        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов: {}", films.size());
        return new ArrayList<>(films.values());
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
