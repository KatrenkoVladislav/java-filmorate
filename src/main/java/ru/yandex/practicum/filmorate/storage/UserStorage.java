package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface UserStorage {

    Map<Long, Set<User>> getFriends();

    User userCreate(User user);

    User userUpdate(User user);

    void userDelete(Long id);

    Collection<User> allUsers();

    User getUser(Long id);
}
