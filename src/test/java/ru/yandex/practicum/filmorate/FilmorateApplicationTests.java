package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/data.sql"})
class FilmoRateApplicationTests {
	private final UserService userService;
	private final FilmService filmService;
	private final GenreStorage genreDao;
	private final RatingMpaStorage ratingMpaStorageDao;

//---USERS---
	User user1 = User.builder()
			.email("ivanov@yandex.ru")
			.login("Ivanov")
			.name("Ivan")
			.birthday(LocalDate.of(1990, 3, 13))
			.build();

	User user2 = User.builder()
			.email("petrov@yandex.ru")
			.login("Petrov")
			.birthday(LocalDate.of(2010, 2, 14))
			.build();
	User user3 = User.builder()
			.email("sidorov@yandex.ru")
			.login("Sidorov")
			.name("Sidor")
			.birthday(LocalDate.of(1985, 12, 10))
			.build();
	User user4 = User.builder()
			.email("alekseev@gmail.com")
			.login("Alekseev")
			.name("Alexey")
			.birthday(LocalDate.of(1989, 10, 11))
			.build();

//---FILMS---
	Film film1 = Film.builder()
			.name("Film1")
			.description("Film1 description")
			.releaseDate(LocalDate.of(1980, 11, 13))
			.duration(120)
			.likes(new HashSet<>())
			.build();
	Film film2 = Film.builder()
			.name("Film2")
			.description("Film2 description")
			.releaseDate(LocalDate.of(1990, 10, 25))
			.duration(90)
			.likes(new HashSet<>())
			.build();
	Film film3 = Film.builder()
			.name("Film3")
			.description("Film3 description")
			.releaseDate(LocalDate.of(1999, 12, 9))
			.duration(180)
			.likes(new HashSet<>())
			.build();


	@BeforeEach
	void beforeEach() {
		initUsers();
		initFilms();
	}

	private void initUsers() {
		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user3);
		userService.addUser(user4);
	}

	private void initFilms() {
		film1.setMpa(filmService.getRatingMpaById(1));
		filmService.addFilm(film1);
		film2.setMpa(filmService.getRatingMpaById(2));
		filmService.addFilm(film2);
		film3.setMpa(filmService.getRatingMpaById(3));
		filmService.addFilm(film3);
	}


	@Test
	void testGetAllUsers() {
		assertEquals(4, userService.getAllUsers().size());
	}

	@Test
	void testGetUserById() {
		assertEquals(user1.getName(), userService.getUserById(1L).getName());
	}

	@Test
	void testAddNewUser() {
		assertEquals(4, userService.getAllUsers().size());
		User user5 = userService.addUser(User.builder()
				.email("user5@gmail.com")
				.login("User5Login")
				.name("User5Name")
				.birthday(LocalDate.of(1991, 3, 22))
				.build());
		assertEquals(5, userService.getAllUsers().size());
		assertEquals(user5.getEmail(), userService.getUserById(5L).getEmail());
	}

	@Test
	void testUpdateUser() {
		user1.setId(1L);
		user1.setFriends(new HashSet<>());
		User expectedUser = User.builder()
				.id(1L)
				.email("gorokhov@yandex.ru")
				.login("Gorokgov")
				.name("Petr")
				.birthday(LocalDate.of(1991, 3, 20))
				.friends(new HashSet<>())
				.build();
		assertEquals(user1, userService.getUserById(1L));
		userService.updateUser(expectedUser);
		assertEquals(expectedUser, userService.getUserById(1L));
	}

	@Test
	void testRemoveUser() {
		assertEquals(4, userService.getAllUsers().size());
		userService.removeUser(1L);
		assertEquals(3, userService.getAllUsers().size());
	}

	@Test
	void testAddFriend() {
		assertEquals(0, userService.getUserById(1L).getFriends().size());
		userService.addFriend(1L, 2L);
		assertEquals(1, userService.getUserById(1L).getFriends().size());
	}

	@Test
	void testRemoveFriend() {
		userService.addFriend(1L, 2L);
		assertEquals(1, userService.getUserById(1L).getFriends().size());
		userService.removeFriend(1L, 2L);
		assertEquals(0, userService.getUserById(1L).getFriends().size());
	}

	@Test
	void testGetUserFriends() {
		userService.addFriend(1L, 2L);
		userService.addFriend(1L, 3L);
		assertEquals(2, userService.getUserById(1L).getFriends().size());
	}

	@Test
	void testGetUserCommonFriends() {
		userService.addFriend(1L, 2L);
		userService.addFriend(1L, 3L);
		userService.addFriend(2L, 3L);
		assertEquals(3L, userService.getUserCommonFriends(1L, 2L).get(0).getId());
	}

	@Test
	void testGetAllFilms() {
		assertEquals(3, filmService.getAllFilms().size());
	}

	@Test
	void testGetFilmById() {
		assertEquals(film1.getName(), filmService.getFilmById(1L).getName());
	}

	@Test
	void testAddNewFilm() {
		assertEquals(3, filmService.getAllFilms().size());
		Film film = filmService.addFilm(Film.builder()
				.name("Film5")
				.description("Film5 description")
				.releaseDate(LocalDate.of(1991, 11, 23))
				.duration(30)
				.mpa(filmService.getRatingMpaById(1))
				.likes(new HashSet<>())
				.build());
		assertEquals(4, filmService.getAllFilms().size());
	}

	@Test
	void testUpdateFilm() {
		film1.setId(1L);
		film1.setGenres(new ArrayList<>());
		Film expectedFilm = Film.builder()
				.id(1L)
				.name("Film updated")
				.description("Film updated description")
				.releaseDate(LocalDate.of(1992, 1, 4))
				.duration(130)
				.mpa(filmService.getRatingMpaById(2))
				.likes(new HashSet<>())
				.genres(new ArrayList<>())
				.build();
		assertEquals(film1, filmService.getFilmById(1L));
		filmService.updateFilm(expectedFilm);
		assertEquals(expectedFilm, filmService.getFilmById(1L));
	}

	@Test
	void testRemoveFilm() {
		assertEquals(3, filmService.getAllFilms().size());
		filmService.removeFilm(1L);
		assertEquals(2, filmService.getAllFilms().size());
	}

	@Test
	void testAddLike() {
		assertEquals(0, filmService.getFilmById(1L).getLikes().size());
		filmService.addLike(1L, 2L);
		assertEquals(1, filmService.getFilmById(1L).getLikes().size());
	}

	@Test
	void testRemoveLike() {
		filmService.addLike(1L, 2L);
		assertEquals(1, filmService.getFilmById(1L).getLikes().size());
		filmService.removeLike(1L, 2L);
		assertEquals(0, filmService.getFilmById(1L).getLikes().size());
	}

	@Test
	void testGetPopularFilms() {
		filmService.addLike(3L, 1L);
		filmService.addLike(3L, 3L);
		filmService.addLike(1L, 3L);

		List<Film> expectedFilms = new ArrayList<>();
		expectedFilms.add(filmService.getFilmById(3L));
		expectedFilms.add(filmService.getFilmById(1L));
		expectedFilms.add(filmService.getFilmById(2L));

		assertEquals(expectedFilms, filmService.getPopularFilms(3));
	}

	@Test
	void testGetAllGenres() {
		assertEquals(6, genreDao.getAllGenres().size());
	}

	@Test
	void testGetGenreById() {
		assertEquals("Драма", genreDao.getGenreById(2).getName());
	}

	@Test
	void testGetFilmGenres() {
		Genre genre1 = genreDao.getGenreById(1);
		Genre genre2 = genreDao.getGenreById(3);
		List<Genre> genres = new ArrayList<>();
		genres.add(genre1);
		genres.add(genre2);
		genreDao.addGenresToFilm((1L), genres);
		assertEquals(genres, filmService.getFilmById(1L).getGenres());
	}

	@Test
	void testAddGenresToFilm() {
		Genre genre = genreDao.getGenreById(1);
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		genreDao.addGenresToFilm((2L), genres);
		assertEquals(genres, filmService.getFilmById(2L).getGenres());
	}

	@Test
	void testUpdateGenresAtFilm() {
		Genre genre1 = genreDao.getGenreById(1);
		List<Genre> genres = new ArrayList<>();
		genres.add(genre1);
		genreDao.addGenresToFilm((3L), genres);
		assertEquals(genres, filmService.getFilmById(3L).getGenres());

		genres.add(genreDao.getGenreById(2));
		genreDao.addGenresToFilm((3L), genres);
		assertEquals(genres, filmService.getFilmById(3L).getGenres());
	}

	@Test
	void testDeleteGenresFromFilm() {
		Genre genre = genreDao.getGenreById(1);
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		genreDao.addGenresToFilm((1L), genres);
		assertEquals(1, filmService.getFilmById(1L).getGenres().size());
		genreDao.deleteGenresFromFilm(1L);
		assertEquals(0, filmService.getFilmById(1L).getGenres().size());
	}

	@Test
	void testGetAllRatingMpa() {
		assertEquals(5, ratingMpaStorageDao.getAllRatingMPA().size());
	}

	@Test
	void testGetRatingMpaById() {
		assertEquals("PG", ratingMpaStorageDao.getRatingMpaById(2).getName());
	}

	@Test
	void testGetFilmRating() {
		assertEquals("G", filmService.getFilmById(1L).getMpa().getName());
	}

}