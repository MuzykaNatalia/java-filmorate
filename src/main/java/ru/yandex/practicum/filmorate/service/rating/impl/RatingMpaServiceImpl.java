package ru.yandex.practicum.filmorate.service.rating.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.rating.RatingMpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.rating.RatingMpaService;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class RatingMpaServiceImpl implements RatingMpaService {
    private final RatingMpaStorage ratingMpaStorage;

    @Override
    public RatingMpa getMpaById(Integer id) {
        RatingMpa mpa = ratingMpaStorage.getMpaById(id);
        if (mpa == null) {
            log.warn("Rating MPA with id={} not found", id);
            throw new NotFoundException(String.format("Rating MPA with id=%d not found", id));
        }
        return mpa;
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        log.info("Received all ratings mpa");
        return ratingMpaStorage.getAllMpa();
    }

    @Override
    public boolean isExistsRatingMpa(Integer id) {
        if (id != null) {
            boolean mpa = ratingMpaStorage.isExistsRatingMpa(id);
            if (!mpa) {
                log.warn("Rating MPA with id={} not already exist", id);
                throw new ValidationException(String.format(
                        "Rating MPA with id=%d not already exist", id));
            }
            return true;
        }
        return false;
    }
}