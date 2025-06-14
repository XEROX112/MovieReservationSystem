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

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @ElementCollection
    @CollectionTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> genre;

    private String description;
    private String about;
    @ElementCollection
    @CollectionTable(name = "movie_language", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> language;

    private Integer duration;

    private LocalDate releaseDate;

    private Certification certification;

    private String poster;
    @ElementCollection
    @CollectionTable(name = "movie_format", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> Format;


    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows;
}