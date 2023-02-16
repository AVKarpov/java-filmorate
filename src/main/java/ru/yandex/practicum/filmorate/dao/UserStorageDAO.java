package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Slf4j
public class UserStorageDAO implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserStorageDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(Long id) {
        try {
            String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
            User user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
            if (user != null)
                user.setFriends(getUserFriendsId(id));
            return user;
        } catch (EmptyResultDataAccessException e) {
            log.info("Получение друзей. Пользователь с id = {} не найден", id);
            throw new NoSuchElementException("GET: Пользователь с таким id не найден");
        }
    }

    private Set<Friendship> getUserFriendsId(Long id) {
        String sqlQuery = "SELECT friend_id, status FROM Friends WHERE user_id = ? ";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToFriendship, id));
    }

    private Friendship mapRowToFriendship(ResultSet resultSet, int rowNum) throws SQLException {
        return new Friendship(resultSet.getLong("friend_id"),
                resultSet.getBoolean("status"));
    }

    @Override
    public User addUser(User user) {
        InMemoryUserStorage.validate(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthdate", user.getBirthday());
        long id = simpleJdbcInsert.executeAndReturnKey(values).longValue();
        log.info("Добавлен пользователь c id = {}", id);
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        InMemoryUserStorage.validate(user);
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthdate = ? " +
                "WHERE user_id = ?";
        if (jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()) == 0) {
            log.info("Пользователь с таким id не найден: {}", user.getId());
            throw new NoSuchElementException("UPDATE: Пользователь с таким id не найден");
        }
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @Override
    public void removeUser(Long id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            throw new NoSuchElementException("DELETE: Пользователь с id = " + id + "не найден");
        }
        log.info("Удалён пользователь c id = {}", id);
    }

    public List<User> getUserNonConfirmedFriends(Long id) {
        String sqlQuery = "SELECT * FROM Users WHERE user_id IN (" +
                "SELECT friend_id FROM Friends WHERE user_id = ? )";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    public void addFriend(Long id, Long friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(new Friendship(friendId, false));
        }
        String sqlQueryUpdateStatus = "UPDATE Friends SET status = TRUE WHERE user_id = ? AND friend_id = ?";
        if (jdbcTemplate.update(sqlQueryUpdateStatus, friendId, id) == 0) {
            //запрос на добавление в друзья пользователь с friendId ранее не отправлял, добавляем новую запись
            String sqlQueryAddFriend = "INSERT INTO Friends (user_id, friend_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQueryAddFriend, id, friendId);

        }
    }

    public void removeFriend(Long id, Long friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().removeIf(data -> data.getId() == friendId);
        }
        String sqlQuery = "DELETE FROM Friends WHERE user_id = ? AND friend_id = ?";
        if (jdbcTemplate.update(sqlQuery, id, friendId) == 0) {
            throw new NoSuchElementException("DELETE: Не удалось удалить друга с friendId = " + friendId
                    + " у пользователя с id = " + id);
        }
        log.info("Удаление друга с friendId = {} у пользователя с id = {}", friendId, id);
    }

    public List<User> getUserCommonNonConfirmedFriends(Long id, Long otherId) {
        String sqlQuery = "SELECT * FROM Users WHERE user_id IN (" +
                "SELECT friend_id FROM Friends WHERE user_id IN (?,?)" +
                "GROUP BY friend_id HAVING COUNT(friend_id) = 2)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
            .id(resultSet.getLong("user_id"))
            .email(resultSet.getString("email"))
            .login(resultSet.getString("login"))
            .name(resultSet.getString("name"))
            .birthday(resultSet.getDate("birthdate").toLocalDate())
            .build();
    }

}
