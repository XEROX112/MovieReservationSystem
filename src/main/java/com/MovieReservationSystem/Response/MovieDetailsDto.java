package com.MovieReservationSystem.Response;

import com.MovieReservationSystem.Model.professionalIdentity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MovieDetailsDto {
    private Long id;
    private String title;
    private List<String> genre;
    private List<professionalIdentity> cast;
    private String description;
    private String about;
    private String poster;
    private Integer duration;
    private List<String> language;
    private List<String> Format;


}
