package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User userCreate(User newUser) {
        return userStorage.userCreate(newUser);
    }

    public Collection<User> allUsers() {
        return userStorage.allUsers();
    }

    public User userUpdate(User newUser) {
        return userStorage.userUpdate(newUser);
    }

    public void userDelete(Long id) {
        userStorage.userDelete(id);
    }

    public void addToFriend(Long userId, Long otherUserId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);
        if (userStorage.getFriends().get(user.getId()).contains(otherUser)) {
            log.error("Ошибка при добавлении друга: Пользователи {} и {} уже являются друзьями.", userId, otherUserId);
            throw new ValidationException("Пользователи уже в друзьях");
        }
        userStorage.getFriends().get(user.getId()).add(userStorage.getUser(otherUserId));
        userStorage.getFriends().get(otherUser.getId()).add(userStorage.getUser(userId));
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, otherUserId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (userStorage.getFriends().get(user.getId()).remove(friend) && userStorage.getFriends().get(friend.getId()).remove(user)) {
            log.info("Пользователь {} успешно удалил пользователя {}", userId, friendId);
        } else {
            log.warn("Пользователь {} не имел друга с ID {}", userId, friendId);
        }
    }

    public List<User> allUserFriends(Long userId) {
        User user = userStorage.getUser(userId);
        Set<User> userFriendsSet = userStorage.getFriends().get(user.getId());
        return new ArrayList<>(userFriendsSet);
    }

    public List<User> commonFriends(Long userId, Long otherUserId) {
        List<User> userFriends = allUserFriends(userId);
        List<User> otherUserFriends = allUserFriends(otherUserId);
        List<User> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);

        return commonFriends;
    }
}
