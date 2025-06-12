package com.MovieReservationSystem.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ElementCollection
    @CollectionTable(name = "movie_cast", joinColumns = @JoinColumn(name = "movie_id"))
    private List<professionalIdentity> cast;



    @ElementCollection
    @CollectionTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> genre;

    private String description;

    @ElementCollection
    @CollectionTable(name = "movie_language", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> language;

    private Integer duration;

    private LocalDate releaseDate;

    private Certification certification;

    private Float rating;

    @ElementCollection
    @CollectionTable(name = "movie_reviews", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> movieReviews;

    private String poster;

    private Format format;


    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows;
}