package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film filmCreate(@RequestBody Film filmRequest) {
        log.info("Пришел Post запрос /films с телом {}", filmRequest);
        Film filmResponse = filmService.filmCreate(filmRequest);
        log.info("Отправлен ответ Post / films с телом {}", filmResponse);
        return filmResponse;

    }

    @PutMapping("{filmId}/like/{userId}")
    public void filmLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Пришел Put запрос c переменной пути /films/{}/like/{}", filmId, userId);
        filmService.filmLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public void filmLikeRemove(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Пришел Delete запрос с переменной пути /films/{}/like/{}", filmId, userId);
        filmService.filmLikeRemove(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("Пришел Get запрос c параметром /films?count={}", count);
        Collection<Film> filmsResponse = filmService.popularFilms(count);
        log.info("Отправлен Get ответ с телом {}", filmsResponse);
        return filmsResponse;
    }

    @PutMapping
    public Film filmUpdate(@RequestBody Film newFilm) {
        log.info("Пришел Put запрос /films с телом {}", newFilm);
        Film filmResponse = filmService.filmUpdate(newFilm);
        log.info("Отправлен Put ответ /films с телом {}", filmResponse);
        return filmResponse;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        Collection<Film> filmsResponse = filmService.allFilms();
        log.info("Отправлен Get ответ /films с телом {}", filmsResponse);
        return filmsResponse;
    }

    @DeleteMapping(value = {"{filmId}"})
    @ResponseStatus(HttpStatus.OK)
    public void filmDelete(@PathVariable Long filmId) {
        log.info("Пришел Delete запрос с переменной пути /films/{}", filmId);
        filmService.filmDelete(filmId);
    }
}
