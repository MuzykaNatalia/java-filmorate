package ru.yandex.practicum.filmorate.service.rating;

import ru.yandex.practicum.filmorate.model.RatingMpa;
import java.util.Collection;

public interface RatingMpaService {
    RatingMpa getMpaById(Integer id);

    Collection<RatingMpa> getAllMpa();

    boolean isExistsRatingMpa(Integer id);
}