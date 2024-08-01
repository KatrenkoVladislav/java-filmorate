package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User userCreate(User user);

    User userUpdate(User user);

    void userDelete(Long id);

    Collection<User> allUsers();

    User getUser(Long id);

    void checkId(Long id);
}
