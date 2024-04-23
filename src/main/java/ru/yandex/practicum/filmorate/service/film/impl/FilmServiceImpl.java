package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.rating.RatingMpaService;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final RatingMpaService ratingMpaService;

    @Override
    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmsById(id);
        if (film == null) {
            log.warn("Film with id={} not found", id);
            throw new FilmNotFoundException(String.format("Film with id=%d not found", id));
        }
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Received all films");
        return filmStorage.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        log.info("Received populars films");
        return filmStorage.getPopularFilm(count);
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getId() != null) {
            log.warn("Incorrect id={} was passed when creating the film: ", film.getId());
            throw new ValidationException("id for the film must not be specified");
        }
        isExistsGenresAndRating(film);
        Film filmCreated = filmStorage.createFilm(film);
        log.info("Create film {}", filmCreated);
        return filmCreated;
    }

    private void isExistsGenresAndRating(Film film) {
        ratingMpaService.isExistsRatingMpa(film.getMpa().getId());
        film.getGenres().forEach(genreFilm -> genreService.isExistsGenres(genreFilm.getId()));
    }

    @Override
    public Film updateFilm(Film film) {
        isExistsIdFilm(film.getId());
        isExistsGenresAndRating(film);
        Film filmUpdated = filmStorage.updateFilm(film);
        log.info("Update film {}", filmUpdated);
        return filmUpdated;
    }

    private void isExistsIdFilm(Long filmId) {
        boolean isExists = filmStorage.isExistsIdFilm(filmId);
        if (!isExists) {
            log.warn("Film with id={} not found", filmId);
            throw new FilmNotFoundException(String.format("Film with id=%d not found", filmId));
        }
    }

    @Override
    public Film putLike(Long id, Long userId) {
        isExistsIdFilm(id);
        log.info("User userId={} liked the film id={}", userId, id);
        return filmStorage.putLike(id, userId);
    }

    @Override
    public Film deleteLike(Long id, Long userId) {
        isExistsIdFilm(id);
        log.info("User id={} removed the like from the film id={}", userId, id);
        return filmStorage.deleteLike(id, userId);
    }

    @Override
    public String deleteFilm(Long id) {
        isExistsIdFilm(id);
        filmStorage.deleteFilm(id);
        log.info("Film with id={} deleted", id);
        return String.format("Film with id=%d deleted", id);
    }
}