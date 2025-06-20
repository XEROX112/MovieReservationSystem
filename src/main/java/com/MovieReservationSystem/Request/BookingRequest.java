package com.MovieReservationSystem.Request;

import com.MovieReservationSystem.Model.Seats;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequest {
    private Long userId;
    private Long showId;
    private List<Seats> requestedSeats;

    // For debugging
    @Override
    public String toString() {
        return "BookingRequest{" +
                "userId=" + userId +
                ", showId=" + showId +
                ", requestedSeats=" + (requestedSeats != null ? requestedSeats.size() + " seats" : "null") +
                '}';
    }
}