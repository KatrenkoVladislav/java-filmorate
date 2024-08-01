package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class FilmServiceTest {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();

    private final UserStorage userStorage = new InMemoryUserStorage();

    private final FilmService filmService = new FilmService(filmStorage, userStorage);

    private User user;
    private User user2;
    private User user3;
    private User user4;

    private User user5;

    private Film film;
    private Film film2;


    @BeforeEach
    public void setupUsers() {
        user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test_user");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage.userCreate(user);

        user2 = new User();
        user2.setEmail("2test2@example.com");
        user2.setLogin("2test2_user2");
        user2.setName("2Test2 2Use2r");
        user2.setBirthday(LocalDate.of(2002, 1, 1));
        userStorage.userCreate(user2);

        user3 = new User();
        user3.setEmail("3test3@example.com");
        user3.setLogin("3test3_3user3");
        user3.setName("3Test3 3User3");
        user3.setBirthday(LocalDate.of(2003, 1, 1));
        userStorage.userCreate(user3);

        user4 = new User();
        user4.setEmail("4test@example4.com");
        user4.setLogin("4test_user4");
        user4.setName("4Test User4");
        user4.setBirthday(LocalDate.of(2004, 1, 1));
        userStorage.userCreate(user4);

        user5 = new User();
        user5.setEmail("5test@example5.com");
        user5.setLogin("5test_user5");
        user5.setName("5Test User5");
        user5.setBirthday(LocalDate.of(2005, 1, 1));
        userStorage.userCreate(user5);

        film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        film2 = new Film();
        film2.setName("2Tes2t Film2");
        film2.setDescription("2Test Descr2ipti2on");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(90);
    }


    @Test
    @DisplayName("Проверяем добавление,повторное добавление, удаление лайка")
    public void canFilmLikeAndRemove() {
        Film addedFilm = filmStorage.filmCreate(film);
        userStorage.userCreate(user);
        userStorage.userCreate(user2);
        filmService.filmLike(addedFilm.getId(), user.getId());
        filmService.filmLike(addedFilm.getId(), user2.getId());
        assertEquals(2, addedFilm.getLikes().size());
        filmService.filmLikeRemove(addedFilm.getId(), user.getId());
        filmService.filmLikeRemove(addedFilm.getId(), user2.getId());
        assertEquals(0, addedFilm.getLikes().size());
    }

    @Test
    @DisplayName("Проверяем популярные фильмы")
    public void canGetPopularFilms() {
        userStorage.userCreate(user);
        userStorage.userCreate(user2);
        userStorage.userCreate(user3);
        userStorage.userCreate(user4);
        userStorage.userCreate(user5);
        Film addedFilm = filmStorage.filmCreate(film);
        Film addedFilm2 = filmStorage.filmCreate(film2);
        filmService.filmLike(addedFilm.getId(), user.getId());
        filmService.filmLike(addedFilm.getId(), user2.getId());
        filmService.filmLike(addedFilm.getId(), user3.getId());
        filmService.filmLike(addedFilm2.getId(), user4.getId());
        filmService.filmLike(addedFilm2.getId(), user5.getId());
        assertEquals(3, addedFilm.getLikes().size());
        assertEquals(2, addedFilm2.getLikes().size());
        Collection<Film> popularFilm = filmService.popularFilms(3L);
        assertTrue(popularFilm.contains(addedFilm));
    }
}
