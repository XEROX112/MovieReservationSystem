package com.MovieReservationSystem.Mapper;

import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.Seats;
import com.MovieReservationSystem.Response.BookingResponseDTO;

import java.util.stream.Collectors;

public class BookingMapper {

    public static BookingResponseDTO mapToDto(Bookings booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setDateTime(booking.getDateTime().toString());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setUsername(booking.getUser().getUsername());
        dto.setEmail(booking.getUser().getEmail());
        dto.setSeatNumbers(
                booking.getSeats()
                        .stream()
                        .map(Seats::getSeatNumber)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
