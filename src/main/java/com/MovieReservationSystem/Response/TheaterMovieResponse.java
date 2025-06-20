package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterMovieResponse {
    private Long TheaterId;
    private String TheaterName;
    private String TheaterLocation;
    private String TheaterRegion;
}
