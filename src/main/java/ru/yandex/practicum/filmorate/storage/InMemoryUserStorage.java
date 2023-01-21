package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long generatorId = 0;

    @Override
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (users.get(id) == null)
            throw new NoSuchElementException("Пользователь с таким id не найден");
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        validate(user);
        user.setId(++generatorId);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь с таким id не найден: {}", user.getId());
            throw new NoSuchElementException("Пользователь с таким id не найден");
        }
        validate(user);
        users.replace(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @Override
    public void removeUser(Long id) {
        if (!users.containsKey(id)) {
            log.info("Невозможно удалить пользователя! Пользователь с таким id не найден: {}", id);
            throw new NoSuchElementException("Невозможно удалить пользователя! Пользователь с таким id не найден");
        }
        users.remove(id);
    }

    public void validate(User user) {
        if (user != null) {
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

}
