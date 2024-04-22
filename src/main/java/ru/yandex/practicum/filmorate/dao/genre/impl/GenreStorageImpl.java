package ru.yandex.practicum.filmorate.dao.genre.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Primary
public class GenreStorageImpl implements GenreStorage {
    private final NamedParameterJdbcOperations parameter;
    protected final String sqlSelectToOneGenre = "SELECT * FROM genre WHERE genre_id = :genreId;";
    protected final String sqlSelectAllGenes = "SELECT * FROM genre ORDER BY genre_id;";

    @Override
    public Genre getGenreById(Integer id) {
        return parameter.query(sqlSelectToOneGenre, Map.of("genreId", id), new GenreMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return parameter.query(sqlSelectAllGenes, new GenreMapper());
    }

    @Override
    public boolean isExistsGenres(Integer id) {
        return getGenreById(id) != null;
    }
}