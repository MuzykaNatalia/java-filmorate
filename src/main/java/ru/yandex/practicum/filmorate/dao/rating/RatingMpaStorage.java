package ru.yandex.practicum.filmorate.dao.rating;

import ru.yandex.practicum.filmorate.model.RatingMpa;
import java.util.Collection;

public interface RatingMpaStorage {
    RatingMpa getMpaById(Integer id);

    Collection<RatingMpa> getAllMpa();

    boolean isExistsRatingMpa(Integer id);
}