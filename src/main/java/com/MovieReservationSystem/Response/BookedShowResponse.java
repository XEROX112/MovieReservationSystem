package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookedShowResponse {
    private Long id;
    private String showDate;  // Could also be LocalDate
    private String showTime;  // Could also be LocalTime
    private BookedMovieResponse movie;
    private BookedTheaterResponse theater;
}
