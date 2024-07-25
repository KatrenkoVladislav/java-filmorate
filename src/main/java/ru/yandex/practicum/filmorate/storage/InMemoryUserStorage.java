package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Getter
    private final Map<Long, Set<User>> friends = new HashMap<>();
    private long id = 0;

    @Override
    public User userCreate(User user) {
        validateUser(user);
        log.info("Пришел Post запрос /users с телом {}", user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        log.info("Отправлен ответ Post /users с телом {}", user);
        return user;
    }

    @Override
    public User userUpdate(User user) {
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

    @Override
    public void userDelete(Long id) {
        if (users.get(id) == null) {
            throw new ConditionsNotMetException("id не найдено");
        }
        users.remove(id);
        log.info("Ползователь {} удален", users.get(id));
    }

    @Override
    public Collection<User> allUsers() {
        log.info("Отправлен ответ Get /films c телом {}", users.values());
        return users.values();
    }

    @Override
    public User getUser(Long id) {
        if (users.get(id) == null) {
            throw new ConditionsNotMetException("айди не найден");
        }
        log.info("Пользователь {} найден", users.get(id));
        return users.get(id);
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
