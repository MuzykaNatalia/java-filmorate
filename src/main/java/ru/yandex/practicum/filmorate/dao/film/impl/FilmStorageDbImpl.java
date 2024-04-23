package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
public class FilmStorageDbImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    protected final String sqlSelectOneFilm = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "WHERE film_id = :filmId;";
    protected final String sqlSelectGenresToOneFilm = "SELECT f.*, g.name " +
            "FROM film_genre AS f " +
            "JOIN genre AS g ON f.genre_id = g.genre_id " +
            "WHERE film_id = :filmId;";
    protected final String sqlSelectAllFilms = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "GROUP BY film_id, rating_name " +
            "ORDER BY film_id;";
    protected final String sqlSelectGenresAllFilms = "SELECT f.*, g.name " +
            "FROM film_genre AS f " +
            "JOIN genre AS g ON f.genre_id = g.genre_id " +
            "ORDER BY g.genre_id;";
    protected final String sqlSelectPopularsFilms = "SELECT f.*, r.name AS rating_name, " +
            "COUNT(l.user_id) AS count_likes " +
            "FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id, rating_name, l.film_id " +
            "ORDER BY COUNT(l.user_id) DESC " +
            "LIMIT :count;";
    protected final String sqlSelectPopularFilmsGenres = "WITH PopularFilms AS (SELECT f.film_id, " +
            "COUNT(ff.user_id) AS like_count " +
            "FROM film f " +
            "LEFT JOIN favorite_film ff ON f.film_id = ff.film_id " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(ff.user_id) DESC " +
            "LIMIT :count) " +
            "SELECT fg.film_id, g.genre_id, g.name " +
            "FROM PopularFilms pf " +
            "JOIN film_genre fg ON pf.film_id = fg.film_id " +
            "JOIN genre g ON fg.genre_id = g.genre_id " +
            "ORDER BY fg.film_id, g.genre_id;";
    protected final String sqlUpdateFilm = "UPDATE film SET name = :name, description = :description, " +
            "release_date = :release_date, duration = :duration, rating_id = :rating_id " +
            "WHERE film_id = :filmId;";
    protected final String sqlDeleteFilm = "DELETE FROM film WHERE film_id = :filmId;";
    protected final String sqlSelectIdFilm = "SELECT film_id FROM film WHERE film_id = :filmId;";
    protected final String sqlInsertLikeFilm = "INSERT INTO favorite_film VALUES (:filmId, :userId);";
    protected final String sqlDeleteLikeFilm = "DELETE FROM favorite_film " +
            "WHERE film_id = :filmId AND user_id = :userId;";
    protected final String sqlInsertGenresFilm = "INSERT INTO film_genre VALUES (:filmId, :genreId);";
    protected final String sqlDeleteGenresFilm = "DELETE FROM film_genre WHERE film_id = :filmId;";

    @Override
    public Film getFilmsById(Long id) {
        Map<String, Object> params = Map.of("filmId", id);
        List<Film> film = parameter.query(sqlSelectOneFilm, params, new FilmMapper());

        if (!film.isEmpty()) {
            return putGenresIntoFilmsWithParams(sqlSelectGenresToOneFilm, params, film).get(0);
        }
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = parameter.query(sqlSelectAllFilms, new FilmMapper());

        if (!films.isEmpty()) {
            return putGenresIntoFilmsWithoutParams(sqlSelectGenresAllFilms, films);
        }
        return films;
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        Map<String, Object> params = Map.of("count", count);
        List<Film> films = parameter.query(sqlSelectPopularsFilms, params, new FilmMapper());

        if (!films.isEmpty()) {
            return putGenresIntoFilmsWithParams(sqlSelectPopularFilmsGenres, params, films);
        }
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert insertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> params = getFilmParams(film);
        film.setId(insertFilm.executeAndReturnKey(params).longValue());
        updateGenre(film);
        return getFilmsById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        Map<String, Object> params = new HashMap<>(getFilmParams(film));
        params.put("filmId", film.getId());
        parameter.update(sqlUpdateFilm, params);
        updateGenre(film);
        return getFilmsById(film.getId());
    }

    @Override
    public Film putLike(Long id, Long userId) {
        parameter.update(sqlInsertLikeFilm, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public Film deleteLike(Long id, Long userId) {
        parameter.update(sqlDeleteLikeFilm, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public void deleteFilm(Long id) {
        parameter.update(sqlDeleteFilm, Map.of("filmId", id));
    }

    @Override
    public boolean isExistsIdFilm(Long filmId) {
        return parameter.query(sqlSelectIdFilm, Map.of("filmId", filmId),
                (rs, rowNum) -> rs.getInt("film_id")).size() > 0;
    }

    private List<Film> putGenresIntoFilmsWithParams(String sqlGenres, Map<String, Object> params, List<Film> film) {
        Map<Long, List<Genre>> genres = parameter.query(sqlGenres, params, new GenreFromFilmMapper());
        return setGenresFilms(film, genres);
    }

    private List<Film> putGenresIntoFilmsWithoutParams(String sqlGenres, List<Film> films) {
        Map<Long, List<Genre>> genres = parameter.query(sqlGenres, new GenreFromFilmMapper());
        return setGenresFilms(films, genres);
    }

    private List<Film> setGenresFilms(List<Film> films, Map<Long, List<Genre>> genres) {
        if (genres != null) {
            return films.stream()
                    .map(film -> {
                        if (genres.containsKey(film.getId())) {
                            film.setGenres(genres.get(film.getId()));
                            return film;
                        }
                        return film;
                    }).collect(Collectors.toList());
        }
        return films;
    }

    private Map<String, Object> getFilmParams(Film film) {
        return Map.of("name", film.getName(), "description", film.getDescription(),
                "release_date", film.getReleaseDate(), "duration", film.getDuration(),
                "rating_id", film.getMpa().getId());
    }

    private void updateGenre(Film film) {
        parameter.update(sqlDeleteGenresFilm, Map.of("filmId", film.getId()));
        if (!film.getGenres().isEmpty()) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            genres.forEach(genre -> parameter.update(sqlInsertGenresFilm,
                    Map.of("filmId", film.getId(), "genreId", genre.getId())));
        }
    }
}