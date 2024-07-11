package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTests {
    @Autowired
    UserController userController;

    @Test
    @DisplayName("Проверяем формат почты")
    void shouldValidationFormatEmail() {
        User user = new User();
        user.setName("Vlad");
        user.setBirthday(LocalDate.of(1993, 4, 24));
        user.setLogin("Botan");
        user.setEmail("");

        assertThrows(ValidationException.class, () -> {
            User user1 = userController.create(user);
        });
    }

    @Test
    @DisplayName("Проверяем формат логина")
    void shouldValidationFormatLogin() {
        User user = new User();
        user.setName("Vlad");
        user.setBirthday(LocalDate.of(1993, 4, 24));
        user.setLogin("");
        user.setEmail("adigey@mail.ru");

        assertThrows(ConstraintViolationException.class, () -> {
            User user1 = userController.create(user);
        });
    }

    @Test
    @DisplayName("Проверяем дату рождения в будущем")
    void shouldValidationBirthdayFuture() {
        User user = new User();

        user.setBirthday(LocalDate.of(2025, 4, 24));
        user.setLogin("Botan");
        user.setEmail("adigey@mail.ru");

        assertThrows(ValidationException.class, () -> {
            User user1 = userController.create(user);
        });
    }

    @Test
    @DisplayName("Проверяем пустое имя")
    void shouldValidationEmptyName() {
        User user = new User();
        user.setBirthday(LocalDate.of(1993, 4, 24));
        user.setLogin("Botan");
        user.setEmail("adigey@mail.ru");
        User user1 = userController.create(user);

        assertEquals("Botan", user1.getName());
    }

    @Test
    @DisplayName("Проверяем правильное написание почты")
    void shouldValidationEmail() {
        User user = new User();
        user.setName("Vlad");
        user.setBirthday(LocalDate.of(1993, 4, 24));
        user.setLogin("Botan");
        user.setEmail("adigeymail.ru@");

        assertThrows(ConstraintViolationException.class, () -> {
            User user1 = userController.create(user);
        });
    }
}
