package com.MovieReservationSystem.Response;

import lombok.Data;

import java.util.List;

@Data
public class MovieResponse {
    private Long id;
    private String title;
    private String poster;
    private List<String> genre;
    private int duration;
    private int totalScreen;
    private List<String> language;
    private List<String> format;
}
