package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("{userId}/friends/{friendId}")
    public void addToFriend(@Valid @PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Пришел Put запрос с переменной пути /users/{}/friends/{}", userId, friendId);
        userService.addToFriend(userId, friendId);
    }

    @DeleteMapping("{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Пришел Delete запрос с переменной пути /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    public List<User> allUserFriends(@PathVariable Long userId) {
        log.info("Пришел Get запрос с переменной пути /users/{}/friends", userId);
        List<User> userFriendsResponse = userService.allUserFriends(userId);
        log.info("Отправлен Get ответ с телом {}", userFriendsResponse);
        return userFriendsResponse;
    }

    @GetMapping("{userId}/friends/common/{otherUserId}")
    public List<User> commonFriends(@PathVariable Long userId, @PathVariable Long otherUserId) {
        log.info("Пришел Get запрос с переменной пути /users/{}/friends/common/{}", userId, otherUserId);
        List<User> userCommonFriendsResponse = userService.commonFriends(userId, otherUserId);
        log.info("Отправлен Get ответ с телом {}", userCommonFriendsResponse);
        return userCommonFriendsResponse;
    }

    @PostMapping
    public User userCreate(@Valid @RequestBody User newUser) {
        log.info("Пришел Post запрос /films с телом {}", newUser);
        User userResponse = userService.userCreate(newUser);
        log.info("Отправлен ответ Post / films с телом {}", userResponse);
        return userResponse;
    }

    @PutMapping
    public User userUpdate(@Valid @RequestBody User newUser) {
        log.info("Пришел Put запрос /films с телом {}", newUser);
        User userResponse = userService.userUpdate(newUser);
        log.info("Отправлен Put ответ /films с телом {}", userResponse);
        return userResponse;
    }

    @GetMapping
    public Collection<User> allUsers() {
        Collection<User> usersResponse = userService.allUsers();
        log.info("Отправлен ответ Get /films c телом {}", usersResponse);
        return usersResponse;
    }

    @DeleteMapping(value = {"{userId}"})
    @ResponseStatus(HttpStatus.OK)
    public void userDelete(@PathVariable("userId") Long id) {
        log.info("Пришел Delete запрос с переменной пути /users/{}", id);
        userService.userDelete(id);
    }
}
