DELETE FROM film_likes;
ALTER TABLE film_likes ALTER COLUMN id RESTART WITH 1;
DELETE FROM film_genre;
ALTER TABLE film_genre ALTER COLUMN id RESTART WITH 1;
DELETE FROM films;
ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1;
DELETE FROM friends;
ALTER TABLE friends ALTER COLUMN friendship_id RESTART WITH 1;
DELETE FROM users;
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
DELETE FROM rating_mpa;
ALTER TABLE rating_mpa ALTER COLUMN rating_id RESTART WITH 1;
DELETE FROM genre;
ALTER TABLE genre ALTER COLUMN genre_id RESTART WITH 1;

INSERT INTO rating_mpa (title)
VALUES ('G'); --у фильма нет возрастных ограничений
INSERT INTO rating_mpa (title)
VALUES ('PG'); --детям рекомендуется смотреть фильм с родителями
INSERT INTO rating_mpa (title)
VALUES ('PG-13'); --детям до 13 лет просмотр не желателен
INSERT INTO rating_mpa (title)
VALUES ('R'); --лицам до 17 лет просматривать фильм можно только в присутствии взрослого
INSERT INTO rating_mpa (title)
VALUES ('NC-17'); --лицам до 18 лет просмотр запрещён

INSERT INTO genre (name)
VALUES ('Комедия');
INSERT INTO genre (name)
VALUES ('Драма');
INSERT INTO genre (name)
VALUES ('Мультфильм');
INSERT INTO genre (name)
VALUES ('Триллер');
INSERT INTO genre (name)
VALUES ('Документальный');
INSERT INTO genre (name)
VALUES ('Боевик');