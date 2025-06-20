package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Seats;
import com.MovieReservationSystem.Request.BookingRequest;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Bookings bookTickets(BookingRequest request);

    List<Bookings> getBookingsByUser(Long userId);

    Optional<Bookings> getBookingById(Long bookingId);

    void cancelBooking(Long bookingId);

    List<Bookings> getAllBookings();

    // Added method for calculating total amount
    Double getPriceAndAssignSeats(Screen screen, List<Seats> seats);
}