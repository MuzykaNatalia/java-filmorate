package ru.yandex.practicum.filmorate.service.rating.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.rating.RatingMpaService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaServiceImplTest {
    private RatingMpaService ratingMpaService;

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