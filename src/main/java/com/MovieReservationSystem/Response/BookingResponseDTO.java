package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private String dateTime;
    private double totalAmount;
    private String username;
    private String email;
    private List<String> seatNumbers;
}
