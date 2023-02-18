package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        validate(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        return userStorage.updateUser(user);
    }

    public void removeUser(Long id) {
        userStorage.removeUser(id);
    }

    //добавление в друзья
    public void addFriend(Long id, Long friendId) {
           userStorage.addFriend(id, friendId);
    }

    // удаление из друзей
    public void removeFriend(Long id, Long friendId) {
        userStorage.removeFriend(id, friendId);
    }

    //получение списка друзей
    public List<User> getUserFriends(Long id) {
        return userStorage.getUserNonConfirmedFriends(id);
    }

    // вывод списка общих друзей
    public List<User> getUserCommonFriends(Long id, Long otherId) {
        return  userStorage.getUserCommonNonConfirmedFriends(id, otherId);
    }

    private void validate(User user) {
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
