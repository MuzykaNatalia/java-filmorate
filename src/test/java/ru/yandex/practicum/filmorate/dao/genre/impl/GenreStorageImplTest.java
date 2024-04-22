package ru.yandex.practicum.filmorate.dao.genre.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreStorageImplTest {
    private GenreStorage genreStorage;

    @DisplayName("Должен вернуть жанр по id")
    @Test
    public void shouldReturnGenreById() {
        Genre genre = new Genre(3, "Мультфильм");

        Genre result = genreStorage.getGenreById(3);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }

    @DisplayName("Должен вернуть все жанры")
    @Test
    public void shouldReturnAllGenres() {
        List<Genre> genres = List.of(
                new Genre(1, "Комедия"), new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"), new Genre(4, "Триллер"),
                new Genre(5, "Документальный"), new Genre(6, "Боевик"));

        Collection<Genre> result = genreStorage.getAllGenres();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genres);
    }
}