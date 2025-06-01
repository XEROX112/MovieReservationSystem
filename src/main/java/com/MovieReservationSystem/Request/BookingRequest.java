package com.MovieReservationSystem.Request;

import com.MovieReservationSystem.Model.Seats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingRequest {
    private Long showId;
    private Long userId;
    private List<Seats> requestedSeats;
}
