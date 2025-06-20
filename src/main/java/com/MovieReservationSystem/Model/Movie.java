package com.MovieReservationSystem.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @Column(length = 1000)
    private String poster;
    @ElementCollection
    @CollectionTable(name = "movie_format", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> Format;


    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows;
}