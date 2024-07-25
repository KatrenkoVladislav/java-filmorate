package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return filmService.filmCreate(filmRequest);
    }

    @PutMapping("{filmId}/like/{userId}")
    public void filmLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.filmLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public void filmLikeRemove(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.filmLikeRemove(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.popularFilms(count);
    }

    @PutMapping
    public Film filmUpdate(@RequestBody Film newFilm) {
        return filmService.filmUpdate(newFilm);
    }

    @GetMapping
    public Collection<Film> allFilms() {
        return filmService.allFilms();
    }

    @DeleteMapping(value = {"{filmId}"})
    public void filmDelete(@PathVariable Long filmId) {
        filmService.filmDelete(filmId);
    }
}
