package com.MovieReservationSystem.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MovieShowsDTO {
    private Long id;
    private String title;
    private String poster;
    private List<ShowDTO> shows = new ArrayList<>();
}
