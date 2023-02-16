package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
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

}
