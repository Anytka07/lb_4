package org.example.repository;


import org.example.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
  @author   Anna Melnychuk
  @project   lb_4
  @class  group 444A
  @version  1.0.0
  @since 05.10.24 - 19.48
*/

@Repository
public interface FilmRepository extends MongoRepository<Film, String> {
}
