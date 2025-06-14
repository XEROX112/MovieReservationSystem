package com.MovieReservationSystem.DTO;

import com.MovieReservationSystem.Model.Certification;
import lombok.Data;

import java.time.LocalDate;

import java.util.List;


@Data
public class MovieDTO {
    private String title;
    private LocalDate year;
    private List<String> genre;
    private int duration;
    private List<String> formats;
    private String description;
    private String about;
    private String poster;
    private List<String> language;
    Certification certification;
    private List<CastDTO> cast;
}
