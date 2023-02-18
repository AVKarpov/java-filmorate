package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class RatingMpaStorageDao implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingMpaStorageDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RatingMpa> getAllRatingMPA() {
        String sqlQuery = "SELECT * FROM rating_mpa ORDER BY rating_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRatingMpa);
    }

    @Override
    public RatingMpa getRatingMpaById(int id) {
        try {
            String sqlQuery = "SELECT * FROM rating_mpa WHERE rating_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRatingMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("GET: Рейтинг MPA с id = " + id + " не найден");
        }
    }

    @Override
    public boolean validateRatingMpaId(Film film) {
        if (film.getMpa() != null)
            return getRatingMpaById(film.getMpa().getId()) != null;
        return false;
    }

    private RatingMpa mapRowToRatingMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new RatingMpa(resultSet.getInt("rating_id"),
                resultSet.getString("title"));
    }
}
