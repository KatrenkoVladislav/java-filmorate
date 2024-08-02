package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private long id = 0;

    @Override
    public Film filmCreate(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film filmUpdate(Film film) {
        getFilm(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> allFilms() {
        return films.values();
    }

    @Override
    public void filmDelete(Long id) {
        Film film = getFilm(id);
        films.remove(film.getId());
        log.info("Фильм {} удален", film);
    }

    @Override
    public Film getFilm(Long id) {
        if (films.get(id) == null) {
            log.info("Фильм по id: {} не найдено", id);
            throw new ConditionsNotMetException("Фильм по id: " + id + " не найден");
        }
        log.info("Фильм {} найден", films.get(id));
        return films.get(id);
    }

    private Long getNextId() {
        return ++id;
    }
}
