package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class FilmStorageDAO implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingMpaDAO ratingMpaDAO;
    private final GenreDAO genreDAO;

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("GET: Фильм с id = " + id + " не найден");
        }
    }

    private Set<Long> getFilmLikes(Long id) {
        String sqlQuery = "SELECT user_id FROM FilmLikes WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToUserId, id));
    }

    private Long mapRowToUserId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получение списка популярных фильмов count = {}", count);
        String sqlQuery = "SELECT f.FILM_ID," +
                "f.TITLE," +
                "f.DESCRIPTION," +
                "f.RELEASE_DATE," +
                "f.DURATION," +
                "f.RATING_ID " +
                "FROM films AS f " +
                "LEFT OUTER JOIN FilmLikes AS fl ON f.FILM_ID = fl.FILM_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(fl.FILM_ID) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Map<String, Object> values = new HashMap<>();
        values.put("title", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        if (ratingMpaDAO.validateRatingMpaId(film))
            values.put("rating_id", film.getMpa().getId());

        long id = simpleJdbcInsert.executeAndReturnKey(values).longValue();
        log.info("Добавлен фильм c id = {}", id);

        if (film.getGenres() != null && film.getGenres().size() > 0) {
            genreDAO.addGenresToFilm(id, film.getGenres());
        }

        return getFilmById(id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "title = ?, description = ?, release_date = ?, duration = ?, rating_id = ?" +
                "WHERE film_id = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) == 0) {
            log.info("Фильм с таким id не найден: {}", film.getId());
            throw new NoSuchElementException("UPDATE: Фильм с id = " + film.getId() + " не найден");
        }
        log.info("Обновлен фильм: {}", film);

        if (film.getGenres() != null) {
            genreDAO.updateGenresAtFilm(film.getId(), film.getGenres());
        }

        return getFilmById(film.getId());
    }

    @Override
    public void removeFilm(Long id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            throw new NoSuchElementException("DELETE: Фильм с таким id не найден");
        }
        log.info("Удалён фильм c id = {}", id);
    }

    public void addLike(Long id, Long userId) {
        String sqlQuery = "INSERT INTO FilmLikes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        String sqlQuery = "DELETE FROM FilmLikes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public List<Genre> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return genreDAO.getGenreById(id);
    }

    public List<RatingMPA> getAllRatingMpa() {
        return ratingMpaDAO.getAllRatingMPA();
    }

    public RatingMPA getRatingMpaById(int id) {
        return ratingMpaDAO.getRatingMpaById(id);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .likes(getFilmLikes(resultSet.getLong("film_id")))
                .genres(genreDAO.getFilmGenres(resultSet.getLong("film_id")))
                .mpa(ratingMpaDAO.getRatingMpaById(resultSet.getInt("rating_id")))
                .build();
    }

}
