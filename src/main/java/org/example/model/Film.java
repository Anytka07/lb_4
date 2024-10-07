package org.example.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/*
  @author   Anna Melnychuk
  @project   lb_4
  @class  group 444A
  @version  1.0.0
  @since 05.10.24 - 19.48
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Film {
    private String id;
    private String title; // Film title
    private String director; // Director of the film
    private String genre; // Genre of the film
    private String description; // Brief description of the film
    private int releaseYear; // Year of release

    public Film(String title, String director, String genre, String description, int releaseYear) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.description = description;
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;
        return getId().equals(film.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
