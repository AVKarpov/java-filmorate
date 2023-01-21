package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void removeUser(Long id) {
        userStorage.removeUser(id);
    }

    //добавление в друзья
    public void addFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id) != null && userStorage.getUserById(friendId) != null) {
            userStorage.getUserById(id).addFriend(friendId);
            userStorage.getUserById(friendId).addFriend(id);
        }
    }

    // удаление из друзей
    public void removeFriend(Long id, Long friendId) {
        if (!userStorage.getUserById(id).getFriends().contains(friendId) ||
                !userStorage.getUserById(friendId).getFriends().contains(id)) {
            log.info("Удаление из друзей. Пользователи с id = {} или friendId = {} не найдены", id, friendId);
            throw new NoSuchElementException(String.format("Пользователи с id = %d или otherId = %d не найдены", id, friendId));
        }
        log.info("Удаление друга с friendId = {} у пользователя с id = {}", friendId, id);
        userStorage.getUserById(id).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(id);
    }

    //получение списка друзей
    public List<User> getUserFriends(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.info("Получение друзей. Пользователь с id = {} не найден", id);
            throw new NoSuchElementException(String.format("Пользователь с id = %d не найден", id));
        }
        log.info("Получение списка друзей пользователя с id: {}", id);
        return userStorage.getAllUsers().stream()
                    .filter(o -> user.getFriends().contains(o.getId()))
                    .collect(Collectors.toList());
    }

    // вывод списка общих друзей
    public List<User> getUserCommonFriends(Long id, Long otherId) {
        User user = userStorage.getUserById(id);
        User other = userStorage.getUserById(otherId);
        if (user == null || other == null) {
            log.info("Получение общих друзей. Пользователи с id = {} и otherId = {} не найдены", id, otherId);
            throw new NoSuchElementException(String.format("Пользователи с id = %d и otherId = %d не найдены", id, otherId));
        }
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(other.getFriends());
        log.info("Получение списка общих друзей пользователей с id и otherId: {}, {}", id, otherId);
        return userStorage.getAllUsers().stream()
                .filter(o -> commonFriendIds.contains(o.getId()))
                .collect(Collectors.toList());
    }

}
