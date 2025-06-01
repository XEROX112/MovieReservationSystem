package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Request.BookingRequest;

import java.util.List;

public interface BookingService {
   public Bookings bookTickets(BookingRequest request);
   public boolean cancelTickets(BookingRequest request);
   public List<Bookings> getBookingsByUserId(Long userId);
}
