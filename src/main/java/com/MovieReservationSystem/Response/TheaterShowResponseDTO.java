package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterShowResponseDTO {
    private Long id;
    private String name;
    private String location;
    private List<String> formats;
    private List<ShowtimeDTO> showtimes;
}
