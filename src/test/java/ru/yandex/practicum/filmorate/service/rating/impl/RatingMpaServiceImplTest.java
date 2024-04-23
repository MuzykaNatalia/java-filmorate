package ru.yandex.practicum.filmorate.service.rating.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.dao.rating.RatingMpaStorage;
import ru.yandex.practicum.filmorate.dao.rating.impl.RatingMpaStorageImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.rating.RatingMpaService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaServiceImplTest {
    private RatingMpaService ratingMpaService;
    private final NamedParameterJdbcOperations parameter;

    @BeforeEach
    public void setUp() {
        RatingMpaStorage ratingMpaStorage = new RatingMpaStorageImpl(parameter);
        ratingMpaService = new RatingMpaServiceImpl(ratingMpaStorage);
    }

    @DisplayName("Должен выдать исключение NotFoundException и не найти id рейтинга МПА")
    @Test
    public void shouldNotFindTheRatingMpaId() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> ratingMpaService.getMpaById(25)
        );
        assertEquals("Rating MPA with id=25 not found", exception.getMessage());
    }
}