package ru.yandex.practicum.filmorate.dao.rating.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.rating.RatingMpaStorage;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RatingMpaStorageImplTest {
    private RatingMpaStorage ratingMpaStorage;
    private final NamedParameterJdbcOperations parameter;

    @BeforeEach
    public void setUp() {
        ratingMpaStorage = new RatingMpaStorageImpl(parameter);
    }

    @DisplayName("Должен вернуть рейтинг MPA по id")
    @Test
    public void shouldReturnRatingMpaById() {
        RatingMpa rating = new RatingMpa(2, "PG");

        RatingMpa result = ratingMpaStorage.getMpaById(2);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(rating);
    }

    @DisplayName("Должен вернуть все рейтинги MPA")
    @Test
    public void shouldReturnAllRatingsMpa() {
        List<RatingMpa> ratings = List.of(
                new RatingMpa(1, "G"), new RatingMpa(2, "PG"),
                new RatingMpa(3, "PG-13"), new RatingMpa(4, "R"),
                new RatingMpa(5, "NC-17"));

        Collection<RatingMpa> result = ratingMpaStorage.getAllMpa();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(ratings);
    }
}