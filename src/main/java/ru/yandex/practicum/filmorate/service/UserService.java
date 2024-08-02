package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User userCreate(User newUser) {
        validateUser(newUser);
        return userStorage.userCreate(newUser);
    }

    public Collection<User> allUsers() {
        return userStorage.allUsers();
    }

    public User userUpdate(User newUser) {
        checkId(newUser.getId());
        validateUser(newUser);
        return userStorage.userUpdate(newUser);
    }

    public void userDelete(Long id) {
        checkId(id);
        userStorage.userDelete(id);
    }

    public void addToFriend(Long userId, Long otherUserId) {
        checkId(userId);
        checkId(otherUserId);
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);
        user.getFriendId().add(otherUserId);
        otherUser.getFriendId().add(userId);
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, otherUserId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkId(userId);
        checkId(friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriendId().remove(friendId);
        friend.getFriendId().remove(userId);
    }

    public List<User> allUserFriends(Long userId) {
        checkId(userId);
        userStorage.getUser(userId);
        return userStorage.getUser(userId).getFriendId().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> commonFriends(Long userId, Long otherUserId) {
        List<User> userFriends = allUserFriends(userId);
        List<User> otherUserFriends = allUserFriends(otherUserId);
        List<User> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);
        return commonFriends;
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

    private void checkId(Long id) {
        if (id == null) {
            log.info("Параметр id не задан в запросе");
            throw new ConditionsNotMetException("Параметр id не задан");
        }
    }
}
