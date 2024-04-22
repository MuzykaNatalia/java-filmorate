package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.rating.RatingMpaService;
import javax.validation.constraints.*;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/mpa")
public class RatingMpaController {
    private final RatingMpaService ratingMpaService;

    @GetMapping("/{id}")
    public RatingMpa getMpaById(@PathVariable @NotNull @Min(1) Integer id) {
        return ratingMpaService.getMpaById(id);
    }

    @GetMapping
    public Collection<RatingMpa> getAllMpa() {
        return ratingMpaService.getAllMpa();
    }
}