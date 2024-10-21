package org.example;

import org.example.model.Film;
import org.example.repository.FilmRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

/*
  @author   Anna Melnychuk
  @project   lb_6
  @class  group 444A
  @version  1.0.0
  @since 22.10.24 - 1:00
*/

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
public class RepositoryTest {

    @Autowired
    FilmRepository underTest;

    @BeforeAll
    void beforeAll() {}

    @BeforeEach
    void setUp() {
        Film gladiator = new Film("1", "Gladiator", "Ridley Scott", "Action", "A Roman general seeks revenge against the corrupt emperor who betrayed him and murdered his family.", 2000);
        Film interstellar = new Film("2", "Interstellar", "Christopher Nolan", "Sci-Fi", "A team of explorers travel through a wormhole...", 2014);
        Film matrix = new Film("3", "The Matrix", "Lana Wachowski, Lilly Wachowski", "Sci-Fi", "A computer hacker learns about the true nature of reality and his role in the war against its controllers.", 1999);
        Film inception = new Film("4", "Inception", "Christopher Cheni", "Thriller", "A skilled thief is given a chance at redemption if he can successfully perform an inception.", 2010);
        Film parasite = new Film("5", "Parasite", "Bong Joon-ho", "Thriller/Drama", "A poor family schemes to become employed by a wealthy family, infiltrating their household.", 2019);

        underTest.saveAll(List.of(gladiator, interstellar, matrix, inception, parasite));
    }

    @AfterEach
    void tearDown() {
        List<Film> filmsToDelete = underTest.findAll().stream()
                .filter(film -> film.getDescription().contains("###test"))
                .toList();
        underTest.deleteAll(filmsToDelete);
    }

    @AfterAll
    void afterAll() {}

    @Test // Перевірка кількості записів у тестовій базі даних
    void testSetShouldContains_2_Records_ToTest(){
        List<Film> filmsToDelete = underTest.findAll().stream()
                .filter(film -> film.getGenre().contains("Sci-Fi"))
                .toList();
        assertEquals(2,filmsToDelete.size());
    }

    @Test // Перевірка, що новий запис фільму отримує унікальний ідентифікатор (ID)
    void shouldGiveIdForNewFilmRecord() {
        // given
        Film inception = new Film("Terror", "Anita Genin", "Horror", "It` very strong and scary movie, about a dangerous travel.", 2010);

        // when
        underTest.save(inception);
        Film filmFromDb = underTest.findAll().stream()
                .filter(film -> film.getTitle().equals("Terror"))
                .findFirst().orElse(null);

        // then
        assertNotNull(filmFromDb);  // Перевірка, що фільм знайдений
        assertNotNull(filmFromDb.getId());  // Перевірка, що в нього є ID
        assertFalse(filmFromDb.getId().isEmpty());  // Перевірка, що ID не порожній
        assertEquals(24, filmFromDb.getId().length());  // Перевірка довжини ID
    }

    @Test // Перевірка видалення фільму
    void shouldDeleteFilmFromRepository() {
        Film filmToDelete = underTest.findById("1").orElse(null);
        assertNotNull(filmToDelete);  // Перевірка, що фільм існує

        underTest.delete(filmToDelete);
        Film deletedFilm = underTest.findById("1").orElse(null);
        assertNull(deletedFilm);  // Перевірка, що фільм видалений
    }

    @Test // Перевірка кількості фільмів
    void shouldContainFiveFilmsInRepository() {
        List<Film> films = underTest.findAll();
        assertEquals(5, films.size());
    }

    @Test // Можливість оновлення інформації про фільм
    void shouldUpdateFilmDescription() {
        Film film = underTest.findById("2").orElse(null);
        assertNotNull(film);

        film.setDescription("A new updated description");
        underTest.save(film);

        Film updatedFilm = underTest.findById("2").orElse(null);
        assertEquals("A new updated description", updatedFilm.getDescription());
    }

    @Test // Пошук фільму за режисером
    void shouldFindFilmByDirector() {
        List<Film> films = underTest.findAll().stream()
                .filter(f -> f.getDirector().equals("Christopher Nolan"))
                .toList();

        assertEquals(1, films.size());
    }

    @Test // Кількість фільмів які вийшли після вказаного року
    void shouldFindFilmsAfter2010() {
        List<Film> films = underTest.findAll().stream()
                .filter(f -> f.getReleaseYear() > 2010)
                .toList();

        assertEquals(2, films.size());  // Перевірка кількості фільмів після 2010 року
    }

    @Test // Пошук фільму за його ID
    void shouldFindFilmById() {
        Film film = underTest.findById("3").orElse(null);

        assertNotNull(film);  // Перевірка, що фільм знайдено
        assertEquals("The Matrix", film.getTitle());  // Перевірка, що знайдено правильний фільм
    }

    @Test // Пошук фільмів, які вийшли у конкретному році
    void shouldFindFilmsByReleaseYear() {
        List<Film> films = underTest.findAll().stream()
                .filter(f -> f.getReleaseYear() == 2000)
                .toList();

        assertEquals(1, films.size());  // Перевірка, що знайдено один фільм
        assertEquals("Gladiator", films.get(0).getTitle());  // Перевірка, що це "Gladiator"
    }


    @Test // Можливість видалення фільмів за жанром
    void shouldDeleteFilmsByGenre() {
        List<Film> filmsToDelete = underTest.findAll().stream()
                .filter(f -> f.getGenre().equals("Thriller"))
                .toList();
        underTest.deleteAll(filmsToDelete);

        List<Film> remainingFilms = underTest.findAll().stream()
                .filter(f -> f.getGenre().equals("Thriller"))
                .toList();

        assertEquals(0, remainingFilms.size());  // Усі фільми жанру "Thriller" мають бути видалені
    }


}
