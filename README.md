# java-filmorate

DB ER-structure of Filmorate project
![Filmorate ER-structure](https://github.com/AVKarpov/java-filmorate/raw/main/Filmorate_diagram.PNG)

**Query examples**
1. Get all films:  
SELECT *  
FROM films;  
2. Get film by id = 19:  
SELECT *  
FROM films  
WHERE film_id = 19;  
3. Get count of likes of film by id = 234:  
SELECT COUNT(film_id)  
FROM FilmLikes  
WHERE film_id = 234;
4. Get names of user friends with id = 14:  
SELECT name  
FROM Users  
WHERE user_id IN (SELECT friend_id  
FROM Friends  
WHERE user_id = 14 AND status = TRUE  
UNION  
SELECT user_id  
FROM Friends  
WHERE friend_id = 14 AND status = TRUE); 
5. Get common friends of user_id = 10 and user_id = 25:  
SELECT name  
FROM Users  
WHERE user_id IN (SELECT friend_id  
FROM Friends  
WHERE (user_id = 10 OR user_id = 25) AND status = TRUE  
UNION  
SELECT user_id  
FROM Friends  
WHERE (friend_id = 10 OR friend_id = 25) AND status = TRUE)  
GROUP BY user_id  
HAVING COUNT(user_id) = 2;  
6. Get top 10 popular films:  
SELECT f.title AS title,
COUNT(fl.film_id) AS total_likes  
FROM FilmLikes AS fl  
LEFT OUTER JOIN Films AS f ON fl.film_id = f.film_id
GROUP BY title  
ORDER BY total_likes DESC  
LIMIT 10;