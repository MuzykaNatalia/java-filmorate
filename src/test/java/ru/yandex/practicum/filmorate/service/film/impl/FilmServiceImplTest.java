package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.rating.RatingMpaService;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceImplTest {
    private FilmService filmService;
    private final GenreService genreService;
    private final RatingMpaService ratingMpaService;
    private final FilmStorage filmStorage;
    private Film film1;

    @BeforeEach
    public void setUp() {
        filmService = new FilmServiceImpl(filmStorage, genreService, ratingMpaService);
        film1 = new Film(null, "555", "555", LocalDate.of(2010, 11, 15), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")));
    }

    @DisplayName("Должен выдать исключение FilmNotFoundException и не найти id фильма")
    @Test
    public void shouldNotFindTheFilmId() {
        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> filmService.getFilmById(25L)
        );
        assertEquals("Film with id=25 not found", exception.getMessage());
    }

    @DisplayName("Не должен обновить фильм и должен выдать исключение FilmNotFoundException")
    @Test
    public void shouldNotUpdateFilmDueToNonExistentId() {
        Film film = new Film(60L,"60", "60",
                LocalDate.of(2000, 12, 12), 60,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")));

        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> filmService.updateFilm(film)
        );
        assertEquals("Film with id=60 not found", exception.getMessage());
    }

    @DisplayName("Не должен обновить фильм и должен выдать исключение ValidationException")
    @Test
    public void shouldNotUpdateFilmDueToNonExistentRatingMpa() {
        filmService.createFilm(film1);
        Film film = new Film(1L,"60", "60",
                LocalDate.of(2000, 12, 12), 60,
                new RatingMpa(1000, "GKLJH"), List.of(new Genre(1, "Комедия")));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.updateFilm(film)
        );
        assertEquals("Rating MPA with id=1000 not already exist", exception.getMessage());
    }
}