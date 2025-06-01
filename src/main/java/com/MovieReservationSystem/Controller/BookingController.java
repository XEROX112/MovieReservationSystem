package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Request.BookingRequest;
import com.MovieReservationSystem.Service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping("/book_ticket")
    public ResponseEntity<Bookings>bookTicket(@RequestBody BookingRequest request){
       Bookings bookings=bookingService.bookTickets(request);
       if(bookings==null){
           return ResponseEntity.notFound().build();
    }
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/get_booking/{id}")
    public ResponseEntity<List<Bookings>>getBookingByUser(@PathVariable Long id){
        List<Bookings> userBookings=bookingService.getBookingsByUserId(id);
        if(userBookings==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userBookings);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBooking(@RequestBody BookingRequest request) {
        boolean deleted = bookingService.cancelTickets(request);
        if (deleted) {
            return ResponseEntity.ok("Booking deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found.");
        }
    }
}
