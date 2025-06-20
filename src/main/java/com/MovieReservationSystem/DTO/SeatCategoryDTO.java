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
public class SeatCategoryDTO {
    private Long categoryId;
    private String categoryName;
    private Integer price;
    private Integer totalSeats;
    private Integer availableSeats;
    private List<String> rows;
    private Integer seatsPerRow;
}
