package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class GenreDAO {
    private final JdbcTemplate jdbcTemplate;

    public GenreDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genre ORDER BY genre_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre getGenreById(int id) {
        try {
            String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("GET: Жанр с id = " + id + " не найден");
        }
    }

    public List<Genre> getFilmGenres(long filmId) {
        String sqlQuery = "SELECT * FROM GENRE WHERE genre_id IN (SELECT genre_id FROM FilmGenre WHERE film_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }

    public void addGenresToFilm(long filmId, List<Genre> genres) {
        validateGenreId(genres);
        String sqlQuery = "INSERT INTO FilmGenre (film_id, genre_id) " +
                "VALUES (?, ?)";
        genres.forEach(genre -> jdbcTemplate.update(sqlQuery,
                filmId,
                genre.getId()));
    }

    public void updateGenresAtFilm(long filmId, List<Genre> genres) {
        deleteGenresFromFilm(filmId);
        addGenresToFilm(filmId, genres);
    }

    public void deleteGenresFromFilm(long film_id) {
        String sqlQuery = "DELETE FROM FilmGenre WHERE film_id = ?";
        if (jdbcTemplate.update(sqlQuery, film_id) == 0) {
            log.info("DELETE: фильм с таким id в таблице FilmGenre не найден: {}", film_id);
        }
    }

    private void validateGenreId(List<Genre> genres) {
        genres.forEach(genre -> getGenreById(genre.getId()));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"),
                resultSet.getString("name"));
    }
}
