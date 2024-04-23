package ru.yandex.practicum.filmorate.service.genre.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public Genre getGenreById(Integer id) {
        Genre genre = genreStorage.getGenreById(id);
        if (genre == null) {
            log.warn("Genre with id={} not found", id);
            throw new NotFoundException(String.format("Genre with id=%d not found", id));
        }
        return genre;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        log.info("Received all genres");
        return genreStorage.getAllGenres();
    }

    @Override
    public boolean isExistsGenres(Integer id) {
        if (id != null) {
            boolean genre = genreStorage.isExistsGenres(id);
            if (!genre) {
                log.warn("Genre with id={} not already exist", id);
                throw new ValidationException(String.format(
                        "Genre with id=%d not already exist", id));
            }
            return true;
        }
        return false;
    }
}