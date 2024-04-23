package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import java.util.Collection;

public interface GenreStorage {
    Genre getGenreById(Integer id);

    Collection<Genre> getAllGenres();

    boolean isExistsGenres(Integer id);
}