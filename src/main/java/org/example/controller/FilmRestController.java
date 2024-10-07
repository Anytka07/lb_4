package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Film;
import org.example.service.FilmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
  @author   Anna Melnychuk
  @project   lb_4
  @class  group 444A
  @version  1.0.0
  @since 05.10.24 - 19.48
*/
@RestController
@RequestMapping("api/v1/films/")
@RequiredArgsConstructor
public class FilmRestController {

    private final FilmService filmService;

    // CRUD - create read update delete

    // read all
    @GetMapping
    public List<Film> showAll() {
        return filmService.getAll();
    }

    // read one
    @GetMapping("{id}")
    public Film showOneById(@PathVariable String id) {
        return filmService.getById(id);
    }

    @PostMapping
    public Film insert(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film edit(@RequestBody Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        filmService.delById(id);
    }

}
