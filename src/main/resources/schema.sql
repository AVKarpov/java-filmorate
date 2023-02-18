CREATE TABLE IF NOT EXISTS rating_mpa (
	rating_id INTEGER AUTO_INCREMENT,
	title VARCHAR(5) NOT NULL,
	CONSTRAINT RATING_MPA_PK PRIMARY KEY (rating_id),
	CONSTRAINT RATING_MPA_UN UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS genre (
	genre_id INTEGER AUTO_INCREMENT,
	name VARCHAR_IGNORECASE(255) NOT NULL,
	CONSTRAINT GENRE_PK PRIMARY KEY (genre_id),
	CONSTRAINT GENRE_UN UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users (
	user_id INTEGER AUTO_INCREMENT,
	email VARCHAR_IGNORECASE(255) NOT NULL,
	login VARCHAR_IGNORECASE(255) NOT NULL,
	name VARCHAR_IGNORECASE(255),
	birthdate DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS friends (
	friendship_id INTEGER AUTO_INCREMENT,
	user_id INTEGER,
	friend_id INTEGER,
	status BOOLEAN DEFAULT FALSE NOT NULL,
	CONSTRAINT FRIENDS_PK PRIMARY KEY (friendship_id),
	CONSTRAINT FRIENDS_FK FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS films (
	film_id INTEGER AUTO_INCREMENT,
	title VARCHAR_IGNORECASE(255) NOT NULL,
	description VARCHAR_IGNORECASE(200),
	release_date DATE NOT NULL,
	duration INTEGER NOT NULL,
	rating_id INTEGER,
	CONSTRAINT FILMS_PK PRIMARY KEY (film_id),
	CONSTRAINT FILMS_FK FOREIGN KEY (rating_id) REFERENCES rating_mpa(rating_id)
);

CREATE TABLE IF NOT EXISTS film_genre (
	id INTEGER AUTO_INCREMENT,
	film_id INTEGER,
	genre_id INTEGER,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (id),
	CONSTRAINT FILM_GENRE_FK FOREIGN KEY (genre_id) REFERENCES genre(genre_id),
	CONSTRAINT FILM_GENRE_FK_1 FOREIGN KEY (film_id) REFERENCES films(film_id)
);

CREATE TABLE IF NOT EXISTS film_likes (
	id INTEGER AUTO_INCREMENT,
	film_id INTEGER,
	user_id INTEGER,
	CONSTRAINT FILM_LIKES_PK PRIMARY KEY (id),
	CONSTRAINT FILM_LIKES_FK FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT FILM_LIKES_FK_1 FOREIGN KEY (film_id) REFERENCES films(film_id)
);