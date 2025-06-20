package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.Mapper.BookingMapper;
import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Seats;
import com.MovieReservationSystem.Model.Show;
import com.MovieReservationSystem.Repository.SeatRepository;
import com.MovieReservationSystem.Repository.ShowRepository;
import com.MovieReservationSystem.Request.BookingRequest;
import com.MovieReservationSystem.Response.BookingResponseDTO;
import com.MovieReservationSystem.Service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ticket")
public class BookingController {

    private final BookingService bookingService;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    public BookingController(BookingService bookingService, SeatRepository seatRepository, ShowRepository showRepository) {
        this.bookingService = bookingService;
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
    }

    @PostMapping(value = "/book_ticket",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
        try {
            // Debug logging
            System.out.println("Received booking request: " + request);

            // Validate request
            if (request.getUserId() == null || request.getShowId() == null) {
                return ResponseEntity.badRequest().body("Missing required fields: userId or showId");
            }

            if (request.getRequestedSeats() == null || request.getRequestedSeats().isEmpty()) {
                return ResponseEntity.badRequest().body("No seats selected");
            }

            Bookings bookings = bookingService.bookTickets(request);
            BookingResponseDTO responseDTO = BookingMapper.mapToDto(bookings);
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            System.err.println("Runtime error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your booking: " + e.getMessage());
        }
    }


    @GetMapping("/get_booking/{id}")
    public ResponseEntity<List<Bookings>> getBookingByUser(@PathVariable Long id) {
        try {
            // Fixed: Changed from getBookingsByUserId to getBookingsByUser
            List<Bookings> userBookings = bookingService.getBookingsByUser(id);
            return ResponseEntity.ok(userBookings);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
        try {
            // Fixed: Changed from cancelTickets to cancelBooking and use bookingId directly
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while canceling the booking");
        }
    }

    // New endpoint to calculate total amount before booking
    @PostMapping("/calculate-total")
    public ResponseEntity<?> calculateTotal(@RequestBody BookingRequest request) {
        try {
            Optional<Show> showOpt = showRepository.findById(request.getShowId());
            if (showOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Show not found");
            }

            Screen screen = showOpt.get().getScreen();

            // Reuse the seat resolving logic
            List<Seats> seats = resolveSeats(request, showOpt.get());

            Double totalAmount = bookingService.getPriceAndAssignSeats(screen, seats);

            return ResponseEntity.ok(Map.of("totalAmount", totalAmount));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while calculating total amount");
        }
    }

    private List<Seats> resolveSeats(BookingRequest request, Show show) {
        if (request.getRequestedSeats() != null && !request.getRequestedSeats().isEmpty()) {
            return request.getRequestedSeats().stream()
                    .map(seat -> seatRepository.findById(seat.getId())
                            .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + seat.getId())))
                    .collect(Collectors.toList());
        } else if (request.getRequestedSeats() != null && !request.getRequestedSeats().isEmpty()) {
            Screen screen = show.getScreen();
            return request.getRequestedSeats().stream()
                    .map(seatNumber -> seatRepository.findBySeatRow_SeatCategory_Screen_AndSeatNumber(screen, String.valueOf(seatNumber))

                            .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNumber)))
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("No seat information provided in booking request");
        }
    }

}