package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.user.impl.UserStorageDbImpl;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmStorageDbImlTest {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private Film film1;
    private Film film2;
    private Film film3;
    private Film film4;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        filmStorage = new FilmStorageDbImpl(jdbcTemplate, parameter);
        userStorage = new UserStorageDbImpl(jdbcTemplate, parameter);
        film1 = new Film(null,"8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")));
        film2 = new Film(null, "Собака киборг", "Собака спасает мир от инопланетян",
                LocalDate.of(2007, 9, 1), 60,
                new RatingMpa(2, "PG"), List.of(new Genre(6, "Боевик")));
        film3 = new Film(null, "Веселые ребята", "Трое друзей отправляются в путешествие",
                LocalDate.of(2013, 4, 26), 80,
                new RatingMpa(3, "PG-13"), List.of(new Genre(1, "Комедия")));
        film4 = new Film(null, "Хитрый лис", "Сказка о лисенке",
                LocalDate.of(2010, 7, 3), 70, new RatingMpa(1, "G"),
                List.of(new Genre(3, "Мультфильм"), new Genre(1, "Комедия")));
        user1 = new User(null, "petrov@email.ru", "vanya123", "Иван Петров",
                LocalDate.of(1990, 1, 1));
        user2 = new User(null, "livanova@email.ru", "liv4mar123", "Мария Ливанова",
                LocalDate.of(1994, 9, 17));
        user3 = new User(null, "nikitin@email.ru", "sr4nik123", "Сергей Никитин",
                LocalDate.of(2000, 12, 24));
    }

    @DisplayName("Должен создать фильм")
    @Test
    public void shouldCreateFilm() {
        Film film = new Film(1L, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"),
                List.of(new Genre(1, "Комедия")));
        filmStorage.createFilm(film1);

        Film result = filmStorage.getFilmsById(1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @DisplayName("Должен обновить фильм")
    @Test
    public void shouldUpdateFilm() {
        Film filmForUpdate = new Film(1L,"миля", "Смит",
                LocalDate.of(2003, 12, 7), 100,
                new RatingMpa(2, "PG"), List.of(new Genre(2, "Драма")));
        filmStorage.createFilm(film1);
        filmStorage.updateFilm(filmForUpdate);

        Film result = filmStorage.getFilmsById(1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmForUpdate);
    }

    @DisplayName("Должен вернуть фильм по id")
    @Test
    public void shouldReturnFilmById() {
        Film film = new Film(1L, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"),
                List.of(new Genre(1, "Комедия")));
        filmStorage.createFilm(film1);

        Film result1 = filmStorage.getFilmsById(1L);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);

        Film result2 = filmStorage.getFilmsById(84L);
        assertThat(result2)
                .isNull();
    }

    @DisplayName("Должен вернуть все фильмы")
    @Test
    public void shouldReturnAllFilms() {
        Film filmNew1 = filmStorage.createFilm(film1);
        Film filmNew2 = filmStorage.createFilm(film2);
        Film filmNew3 = filmStorage.createFilm(film3);
        Film filmNew4 = filmStorage.createFilm(film4);
        List<Film> films = List.of(filmNew1, filmNew2, filmNew3, filmNew4);

        Collection<Film> result = filmStorage.getAllFilms();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @DisplayName("Должен вернуть 2 популярных фильма")
    @Test
    void shouldReturnPopularFilms() {
        Film filmResult1 = new Film(1L, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")));
        Film filmResult3 = new Film(3L, "Веселые ребята", "Трое друзей отправляются в путешествие",
                LocalDate.of(2013, 4, 26), 80,
                new RatingMpa(3, "PG-13"), List.of(new Genre(1, "Комедия")));
        List<Film> films = List.of(filmResult3, filmResult1);

        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        filmStorage.createFilm(film3);
        filmStorage.createFilm(film4);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        filmStorage.putLike(3L, 1L);
        filmStorage.putLike(3L, 2L);
        filmStorage.putLike(3L, 3L);
        filmStorage.putLike(1L, 1L);
        filmStorage.putLike(1L, 3L);
        filmStorage.putLike(4L, 2L);

        Collection<Film> result = filmStorage.getPopularFilm(2);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @DisplayName("Должен добавить лайк фильму с id = 1 от пользователя с id = 1")
    @Test
    void shouldPutLikeFilm() {
        Film film = new Film(1L, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")));

        filmStorage.createFilm(film1);
        userStorage.createUser(user1);

        Film result = filmStorage.putLike(1L, 1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @DisplayName("Должен удалить лайк фильму с id = 1 от пользователя с id = 1")
    @Test
    void shouldDeleteLikeFilm() {
        Film film = new Film(1L, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")));

        filmStorage.createFilm(film1);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        filmStorage.putLike(1L, 1L);
        filmStorage.putLike(1L, 2L);

        Film result = filmStorage.deleteLike(1L, 1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @DisplayName("Должен удалить фильм с id = 1")
    @Test
    void shouldDeleteFilm() {
        List<Film> films = List.of(film2);
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        filmStorage.deleteFilm(1L);

        Collection<Film> result = filmStorage.getAllFilms();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @DisplayName("Должен проверить существует ли id фильма")
    @Test
    void shouldCheckForExistenceOfFilmId() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);

        boolean resultFirst = filmStorage.isExistsIdFilm(1L);
        assertThat(resultFirst)
                .isNotNull()
                .isEqualTo(true);

        boolean resultSecond = filmStorage.isExistsIdFilm(358L);
        assertThat(resultSecond)
                .isNotNull()
                .isEqualTo(false);
    }
}