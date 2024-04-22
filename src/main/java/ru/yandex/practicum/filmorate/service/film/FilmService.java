package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.*;
import java.util.Collection;

public interface FilmService {
    Film getFilmById(Long id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count);

    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Collection<RatingMpa> getAllMpa();

    RatingMpa getMpaById(Integer id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film putLike(Long id, Long userId);

    Film deleteLike(Long id, Long userId);

    String deleteFilm(Long id);
}