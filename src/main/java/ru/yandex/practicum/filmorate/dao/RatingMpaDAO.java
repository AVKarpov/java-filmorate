package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class RatingMpaDAO {
    private final JdbcTemplate jdbcTemplate;

    public RatingMpaDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RatingMPA> getAllRatingMPA() {
        String sqlQuery = "SELECT * FROM ratingmpa ORDER BY rating_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRatingMPA);
    }

    public RatingMPA getRatingMpaById(int id) {
        try {
            String sqlQuery = "SELECT * FROM ratingmpa WHERE rating_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRatingMPA, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("GET: Рейтинг MPA с id = " + id + " не найден");
        }
    }

    public boolean validateRatingMpaId(Film film) {
        if (film.getMpa() != null)
            return getRatingMpaById(film.getMpa().getId()) != null;
        return false;
    }

    private RatingMPA mapRowToRatingMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return new RatingMPA(resultSet.getInt("rating_id"),
                resultSet.getString("title"));
    }
}
