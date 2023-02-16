CREATE TABLE IF NOT EXISTS RATINGMPA (
	RATING_ID INTEGER AUTO_INCREMENT,
	TITLE VARCHAR(5) NOT NULL,
	CONSTRAINT RATINGMPA_PK PRIMARY KEY (RATING_ID),
	CONSTRAINT RATINGMPA_UN UNIQUE (TITLE)
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE (
	GENRE_ID INTEGER AUTO_INCREMENT,
	NAME VARCHAR_IGNORECASE(255) NOT NULL,
	CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID),
	CONSTRAINT GENRE_UN UNIQUE (NAME)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
	USER_ID INTEGER AUTO_INCREMENT,
	EMAIL VARCHAR_IGNORECASE(255) NOT NULL,
	LOGIN VARCHAR_IGNORECASE(255) NOT NULL,
	NAME VARCHAR_IGNORECASE(255),
	BIRTHDATE DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS (
	FRIENDSHIP_ID INTEGER AUTO_INCREMENT,
	USER_ID INTEGER,
	FRIEND_ID INTEGER,
	STATUS BOOLEAN DEFAULT FALSE NOT NULL,
	CONSTRAINT FRIENDS_PK PRIMARY KEY (FRIENDSHIP_ID),
	CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID),
	CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS(USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
	FILM_ID INTEGER AUTO_INCREMENT,
	TITLE VARCHAR_IGNORECASE(255) NOT NULL,
	DESCRIPTION VARCHAR_IGNORECASE(200),
	RELEASE_DATE DATE NOT NULL,
	DURATION INTEGER NOT NULL,
	RATING_ID INTEGER,
	CONSTRAINT FILMS_PK PRIMARY KEY (FILM_ID),
	CONSTRAINT FILMS_FK FOREIGN KEY (RATING_ID) REFERENCES PUBLIC.RATINGMPA(RATING_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMGENRE (
	ID INTEGER AUTO_INCREMENT,
	FILM_ID INTEGER,
	GENRE_ID INTEGER,
	CONSTRAINT FILMGENRE_PK PRIMARY KEY (ID),
	CONSTRAINT FILMGENRE_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(GENRE_ID),
	CONSTRAINT FILMGENRE_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMLIKES (
	ID INTEGER AUTO_INCREMENT,
	FILM_ID INTEGER,
	USER_ID INTEGER,
	CONSTRAINT FILMLIKES_PK PRIMARY KEY (ID),
	CONSTRAINT FILMLIKES_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID),
	CONSTRAINT FILMLIKES_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID)
);