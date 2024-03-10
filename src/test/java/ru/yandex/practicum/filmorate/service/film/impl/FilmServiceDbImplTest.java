package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDb;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceDbImplTest {
    private FilmService filmService;
    private final FilmStorageDb filmStorage;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void setUp() {
        filmService = new FilmServiceDbImpl(filmStorage);
        film1 = new Film(1,"1", "1", LocalDate.of(2000, 12, 12), 60,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")));
        film2 = new Film("555", "555", LocalDate.of(2010, 11, 15), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")));
    }

    @DisplayName("Должен выдать исключение FilmNotFoundException и не найти id фильма")
    @Test
    public void shouldNotFindTheFilmId() {
        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> filmService.getFilmById(25)
        );
        assertEquals("Film with id=25 not found", exception.getMessage());
    }

    @DisplayName("Должен выдать исключение NotFoundException и не найти id жанра")
    @Test
    public void shouldNotFindTheGenreId() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.getGenreById(25)
        );
        assertEquals("Genre with id=25 not found", exception.getMessage());
    }

    @DisplayName("Должен выдать исключение NotFoundException и не найти id рейтинга МПА")
    @Test
    public void shouldNotFindTheRatingMpaId() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.getMpaById(25)
        );
        assertEquals("Rating MPA with id=25 not found", exception.getMessage());
    }

    @DisplayName("Не должен создать фильм и должен выдать исключение FilmAlreadyExistException")
    @Test
    public void shouldNotCreateFilm() {
        FilmAlreadyExistException exception = assertThrows(
                FilmAlreadyExistException.class,
                () -> filmService.createFilm(film1)
        );
        assertEquals("Film id=1 \"1\"already exist", exception.getMessage());
    }

    @DisplayName("Не должен обновить фильм и должен выдать исключение FilmNotFoundException")
    @Test
    public void shouldNotUpdateFilmDueToNonExistentId() {
        Film film_1 = new Film(60,"60", "60",
                LocalDate.of(2000, 12, 12), 60,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")));

        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> filmService.updateFilm(film_1)
        );
        assertEquals("Film with id=60 not found", exception.getMessage());
    }

    @DisplayName("Не должен обновить фильм и должен выдать исключение ValidationException")
    @Test
    public void shouldNotUpdateFilmDueToNonExistentRatingMpa() {
        filmService.createFilm(film2);
        Film film_1 = new Film(1,"60", "60",
                LocalDate.of(2000, 12, 12), 60,
                new RatingMpa(1000, "GKLJH"), List.of(new Genre(1, "Комедия")));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.updateFilm(film_1)
        );
        assertEquals("Rating MPA with id=1000 not already exist", exception.getMessage());
    }
}