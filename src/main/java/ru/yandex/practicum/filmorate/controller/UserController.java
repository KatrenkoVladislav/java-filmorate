package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @GetMapping
    public Collection<User> getAll() {
        log.info("Отправлен ответ Get /films c телом {}", users.values());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        log.info("Пришел Post запрос /users с телом {}", user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Отправлен ответ Post /users с телом {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.info("Фильм с id {} не найден!", user.getId());
            throw new ConditionsNotMetException("id не найдено");
        }
        log.info("Пришел Put запрос /users с телом {}", user);
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Отправлен ответ Put /users с телом {}", user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Ошибка в написании почты");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Ошибка в написании логина");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка в дате рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    private long getNextId() {
        return ++id;
    }
}
