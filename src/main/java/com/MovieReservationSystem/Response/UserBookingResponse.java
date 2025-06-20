package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBookingResponse {
    private Long id;
    private LocalDateTime dateTime;
    private Double totalAmount;
    private BookedShowResponse show;
    private List<BookedSeatResponse> seats;
}
