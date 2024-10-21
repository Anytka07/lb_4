package org.example.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.model.Film;
import org.example.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
  @author   Anna Melnychuk
  @project   lb_4
  @class  group 444A
  @version  1.0.0
  @since 05.10.24 - 19.48
*/

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    private final List<Film> films = new ArrayList<>();
    {
        films.add(new Film("1", "name1", "tttt", "Action/Drama", ".", 2000));
        films.add(new Film("2", "name2", "2222", "Sci-Fi", ".", 2014));
        films.add(new Film("3", "name3", "3333, Lilly Wachowski", "Sci-Fi", ".", 1999));
    }

    @PostConstruct
    void init() {
        filmRepository.deleteAll();
        filmRepository.saveAll(films);
    }

    // CRUD methods

    public List<Film> getAll() {
        return filmRepository.findAll();
    }

    public Film getById(String id) {
        return filmRepository.findById(id).orElse(null);
    }

    public Film create(Film film) {
        return filmRepository.save(film);
    }

    public Film update(Film film) {
        return filmRepository.save(film);
    }

    public void delById(String id) {
        filmRepository.deleteById(id);
    }

}
