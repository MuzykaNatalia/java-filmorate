package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.*;
import java.util.Collection;

public interface FilmStorage {
    Film getFilmsById(Integer id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count);

    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Collection<RatingMpa> getAllMpa();

    RatingMpa getMpaById(Integer id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer id);

    Film putLike(Integer id, Long userId);

    Film deleteLike(Integer id, Long userId);

    boolean isExistsIdFilm(Integer filmId);
}