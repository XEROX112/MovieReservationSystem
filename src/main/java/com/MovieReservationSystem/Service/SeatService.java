package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.SeatRow;
import com.MovieReservationSystem.Model.Seats;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SeatService {
    public void createSeatsInRow(SeatRow seatRow, int columns);
    public List<Seats> getSeatsByRow(Long rowId);
}
