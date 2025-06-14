package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.SeatRequest;
import com.MovieReservationSystem.Model.SeatRow;
import com.MovieReservationSystem.Model.Seats;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SeatService {
    public void createSeatsInRow(SeatRequest request, SeatRow seatRow);

    public List<Seats> getSeatsByRow(Long rowId);
}
