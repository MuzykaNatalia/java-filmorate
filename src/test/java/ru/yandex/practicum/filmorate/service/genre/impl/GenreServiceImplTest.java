package ru.yandex.practicum.filmorate.service.genre.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreServiceImplTest {
    private GenreService genreService;

    @DisplayName("Должен выдать исключение NotFoundException и не найти id жанра")
    @Test
    public void shouldNotFindTheGenreId() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> genreService.getGenreById(25)
        );
        assertEquals("Genre with id=25 not found", exception.getMessage());
    }
}