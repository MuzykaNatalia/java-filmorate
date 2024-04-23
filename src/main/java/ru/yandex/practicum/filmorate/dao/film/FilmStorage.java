package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.*;
import java.util.Collection;

public interface FilmStorage {
    Film getFilmsById(Long id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long id);

    Film putLike(Long id, Long userId);

    Film deleteLike(Long id, Long userId);

    boolean isExistsIdFilm(Long filmId);
}