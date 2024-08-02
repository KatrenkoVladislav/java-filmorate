package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film filmCreate(Film filmRequest) {
        validateFilm(filmRequest);
        return filmStorage.filmCreate(filmRequest);
    }

    public Collection<Film> allFilms() {
        return filmStorage.allFilms();
    }

    public Film filmUpdate(Film newFilm) {
        checkId(newFilm.getId());
        validateFilm(newFilm);
        return filmStorage.filmUpdate(newFilm);
    }

    public void filmDelete(Long id) {
        checkId(id);
        filmStorage.filmDelete(id);
    }

    public void filmLike(Long filmId, Long userId) {
        checkId(filmId);
        checkId(userId);
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        film.getLikes().add(userId);
        log.info("Фильму {} был поставлен лайк от пользователя {}", film, user);
    }

    public void filmLikeRemove(Long filmId, Long userId) {
        checkId(filmId);
        checkId(userId);
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        film.getLikes().remove(userId);
        log.info("У фильма {} был удален лайк от пользователя {}", film, user);
    }

    public Collection<Film> popularFilms(Long count) {
        log.info("Было возращено {} популярных фильма", count);
        return filmStorage.allFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList()).reversed();
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

    private void checkId(Long id) {
        if (id == null) {
            log.info("Параметр id не задан в запросе");
            throw new ConditionsNotMetException("Параметр id не задан");
        }
    }
}
