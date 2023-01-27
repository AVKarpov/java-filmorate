package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	static InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
	static InMemoryUserStorage userStorage = new InMemoryUserStorage();

	@Test
	void testFilmEmptyNameValidation() {
		Film film = new Film();
		Exception exception = assertThrows(ValidationException.class, () -> filmStorage.validate(film));
		assertEquals("Название фильма не может быть пустым", exception.getMessage());

		film.setName("");
		exception = assertThrows(ValidationException.class, () -> filmStorage.validate(film));
		assertEquals("Название фильма не может быть пустым", exception.getMessage());
	}

	@Test
	void testFilmMaxDescriptionLengthValidation() {
		Film film = new Film();
		film.setName("Test");
		film.setDescription("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest" +
				"testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest" +
				"testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest" +
				"testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest" +
				"testtesttesttesttesttesttesttesttesttesttesttesttesttesttest");
		Exception exception = assertThrows(ValidationException.class, () -> filmStorage.validate(film));
		assertEquals("Максимальная длина описания не должна превышать 200 символов", exception.getMessage());
	}

	@Test
	void testFilmDurationValidation() {
		Film film = new Film();
		film.setName("Test");
		film.setDescription("TestDescription");
		film.setDuration(0L);
		Exception exception = assertThrows(ValidationException.class, () -> filmStorage.validate(film));
		assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
	}

	@Test
	void testFilmReleaseDateValidation() {
		Film film = new Film();
		film.setName("Test");
		film.setDescription("TestDescription");
		film.setDuration(200);
		film.setReleaseDate(LocalDate.of(1850,10,2));
		Exception exception = assertThrows(ValidationException.class, () -> filmStorage.validate(film));
		assertEquals("Дата релиза не корректная", exception.getMessage());
	}

	@Test
	void testUserEmailValidation() {
		User user = new User();
		Exception exception = assertThrows(ValidationException.class, () -> userStorage.validate(user));
		assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());

		user.setEmail(" ");
		exception = assertThrows(ValidationException.class, () -> userStorage.validate(user));
		assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());

		user.setEmail("test_yandex.ru");
		exception = assertThrows(ValidationException.class, () -> userStorage.validate(user));
		assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());

	}

	@Test
	void testUserLoginValidation() {
		User user = new User();
		user.setEmail("test@yandex.ru");
		Exception exception = assertThrows(ValidationException.class, () -> userStorage.validate(user));
		assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());

		user.setLogin("test login ");
		exception = assertThrows(ValidationException.class, () -> userStorage.validate(user));
		assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
	}

	@Test
	void testUserEmptyNameValidation() {
		User user = new User();
		user.setEmail("test@yandex.ru");
		user.setLogin("TestLogin");
		user.setBirthday(LocalDate.of(2000,12,10));
        userStorage.validate(user);
		assertEquals("TestLogin", user.getName());
	}

	@Test
	void testUserBirthdayValidation() {
		User user = new User();
		user.setEmail("test@yandex.ru");
		user.setLogin("TestLogin");
		user.setName("TestName");
		user.setBirthday(LocalDate.of(2100,12,10));
		Exception exception = assertThrows(ValidationException.class, () -> userStorage.validate(user));
		assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
	}

}
