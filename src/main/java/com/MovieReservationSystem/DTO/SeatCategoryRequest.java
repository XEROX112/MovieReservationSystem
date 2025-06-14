package com.MovieReservationSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatCategoryRequest {
    private String categoryName;
    private double price;
    private List<RowRequest> seatRows;
}

