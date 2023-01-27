package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();
    User getUserById(Long id);
    User addUser(User user);
    User updateUser(User user);
    void removeUser(Long id);
}
