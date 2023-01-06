package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        user.setId(++generatorId);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь с таким id не найден: {}", user.getId());
            throw new ValidationException("Пользователь с таким id не найден");
        }
        validate(user);
//        if (user.getName() == null || user.getName().isBlank())
//            user.setName(user.getLogin());
        users.replace(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public void validate(User user) {
        //электронная почта не может быть пустой и должна содержать символ @
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Электронная почта пользователя либо пустая, либо не содержит @: {}", user);
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        //логин не может быть пустым и содержать пробелы
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.info("Логин пустой или содержит пробелы: {}", user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        //дата рождения не может быть в будущем
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения в будущем: {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

    }

}
