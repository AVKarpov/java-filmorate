# java-filmorate

DB ER-structure of Filmorate project
![Filmorate ER-structure](https://github.com/AVKarpov/java-filmorate/raw/main/Filmorate_diagram.PNG)

Query examples:
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
WHERE user_id = 14);
