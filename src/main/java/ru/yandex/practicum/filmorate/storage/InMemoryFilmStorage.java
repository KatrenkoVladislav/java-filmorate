package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Getter
    private final Map<Long, Set<Long>> likes = new HashMap<>();
    private long id = 0;

    @Override
    public Film filmCreate(Film film) {
        log.info("Пришел Post запрос /films с телом {}", film);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        log.info("Отправлен ответ Post / films с телом {}", film);
        return film;
    }

    @Override
    public Film filmUpdate(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.info("Фильм с id {} не найден!", film.getId());
            throw new ConditionsNotMetException("не верно указан id=" + film.getId());
        }
        log.info("Пришел Put запрос /films с телом {}", film);
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Отправлен ответ Put / films с телом {}", film);
        return film;
    }

    @Override
    public Collection<Film> allFilms() {
        log.info("Отправлен ответ Get /films c телом {}", films.values());
        return films.values();
    }

    @Override
    public void filmDelete(Long id) {
        if (films.get(id) == null) {
            throw new ConditionsNotMetException("id не найдено");
        }
        films.remove(id);
        log.info("Фильм {} удален", films.get(id));
    }

    @Override
    public Film getFilm(Long id) {
        if (films.get(id) == null) {
            throw new ConditionsNotMetException("айди не найден");
        }
        log.info("Фильм {} найден", films.get(id));
        return films.get(id);
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

    private Long getNextId() {
        return ++id;
    }
}
