package com.MovieReservationSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowRequest {
    private String rowName;
    private int columnCount;
    private List<SeatRequest> seats;
}

