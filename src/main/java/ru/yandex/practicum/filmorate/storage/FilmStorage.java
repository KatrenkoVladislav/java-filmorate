package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {

    Film filmCreate(Film film);

    Film filmUpdate(Film film);

    Collection<Film> allFilms();

    void filmDelete(Long id);

    Film getFilm(Long id);
}
