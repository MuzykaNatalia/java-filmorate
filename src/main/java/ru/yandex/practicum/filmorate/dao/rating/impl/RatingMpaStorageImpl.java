package ru.yandex.practicum.filmorate.dao.rating.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.rating.RatingMpaStorage;
import ru.yandex.practicum.filmorate.mapper.RatingMpaMapper;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Primary
public class RatingMpaStorageImpl implements RatingMpaStorage {
    private final NamedParameterJdbcOperations parameter;
    protected final String sqlSelectToOneRatingMpa = "SELECT * FROM rating WHERE rating_id = :ratingId;";
    protected final String sqlSelectAllRatingMpa = "SELECT * FROM rating ORDER BY rating_id;";

    @Override
    public RatingMpa getMpaById(Integer id) {
        return parameter.query(sqlSelectToOneRatingMpa, Map.of("ratingId", id), new RatingMpaMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        return parameter.query(sqlSelectAllRatingMpa, new RatingMpaMapper());
    }

    @Override
    public boolean isExistsRatingMpa(Integer id) {
        return getMpaById(id) != null;
    }
}