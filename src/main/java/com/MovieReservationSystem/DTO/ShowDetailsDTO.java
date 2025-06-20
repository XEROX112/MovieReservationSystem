package com.MovieReservationSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowDetailsDTO {
    private Long showId;
    private String showTime;
    private String showDate;
    private String movieTitle;
    private String moviePoster;
    private Long screenId;
    private String screenName;
    private String theaterName;
    private List<SeatCategoryDTO> seatCategories;
}
