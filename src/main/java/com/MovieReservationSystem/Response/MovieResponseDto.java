package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponseDto {
    private Long id;
    private String title;
    private Double rating;
    private Integer year;
    private List<String> genre;
    private String duration; // "181 min"
    private List<String> language;
    private List<String> format;
    private String description;
    private String about;
    private String poster;
    private List<CastDto> cast;
}
