package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Все фильмы");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Пришел Post запрос /films с телом {}", film);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Отправлен ответ Post / films с телом {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.info("Фильм с id {} не найден!", film.getId());
            throw new ConditionsNotMetException("id не найдено");
        }
        validateFilm(film);
        log.info("Пришел Put запрос /films с телом {}", film);
        films.put(film.getId(), film);
        log.info("Отправлен ответ Put / films с телом {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().trim().isEmpty()) {
            log.error("Ошибка в названии фильма!");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Превышена длина описания!");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Ошибка в дате релиза!");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность отрицательная!");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private long getNextId() {
        return ++id;
    }
}
