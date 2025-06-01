package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Model.*;
import com.MovieReservationSystem.Repository.BookingRepository;
import com.MovieReservationSystem.Repository.SeatRepository;
import com.MovieReservationSystem.Repository.ShowRepository;
import com.MovieReservationSystem.Repository.UserRepository;
import com.MovieReservationSystem.Request.BookingRequest;
import com.MovieReservationSystem.Service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImplementation implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    public BookingServiceImplementation(BookingRepository bookingRepository, UserRepository userRepository, ShowRepository showRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public Bookings bookTickets(BookingRequest request) {
        Optional<Show> showOpt = showRepository.findById(request.getShowId());

        if (showOpt.isEmpty()) {
             throw  new  RuntimeException("Show not found");
        }

        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        Show show = showOpt.get();
        Boolean isSeatAvailable = isSeatAvailable(show.getScreen(), request.getRequestedSeats());

        if (!isSeatAvailable) {
            throw new  RuntimeException("Please select other Seats");
        }
        Double totalBookingPrice=getPriceAndAssignSeats(show.getScreen(),request.getRequestedSeats());

        Bookings bookings = new Bookings();
        bookings.setShow(show);
        bookings.setUser(user);
        bookings.setSeats(request.getRequestedSeats());
        bookings.setTotalAmount(totalBookingPrice);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime customDateTime = now.withHour(18).withMinute(30).withSecond(0).withNano(0);

        bookings.setDateTime(customDateTime);

        user.getBookings().add(bookings);
        userRepository.save(user);

        show.getBookings().add(bookings);
        showRepository.save(show);
        return bookingRepository.save(bookings);
    }

    @Transactional
    public Double getPriceAndAssignSeats(Screen screen, List<Seats> requestedSeats) {
        double totalPrice = 0.0;

        // Fetch the latest seats from the database using their IDs
        List<Long> seatIds = requestedSeats.stream()
                .map(Seats::getId)  // Assuming getId() fetches the seat ID
                .toList();
        List<Seats> seatsFromDB = seatRepository.findAllById(seatIds);

        for (Seats seat : seatsFromDB) {
            double seatPrice = screen.getSeatCategories().stream()
                    .filter(category -> category.getSeatRows().stream()
                            .anyMatch(row -> row.getSeats().contains(seat)))
                    .map(SeatCategory::getPrice)
                    .findFirst()
                    .orElse(0);

            totalPrice += seatPrice;

            // Mark seat as unavailable
            seat.setIsAvailable(false);
        }

        // Update in DB
        seatRepository.saveAll(seatsFromDB);

        return totalPrice;
    }



    private Boolean isSeatAvailable(Screen screen, List<Seats> requestedSeats) {
        for (Seats seat : requestedSeats) {
            // Check if the seat exists in the screen and is available
            boolean seatExistsAndAvailable = screen.getSeatCategories().stream()
                    .flatMap(category -> category.getSeatRows().stream())
                    .flatMap(row -> row.getSeats().stream())
                    .anyMatch(s -> s.getId().equals(seat.getId()) && s.getIsAvailable());

            // If any seat is not available, return false
            if (!seatExistsAndAvailable) {
                return false;
            }
        }
        return true; // All requested seats are available
    }


    @Override
    @Transactional
    public boolean cancelTickets(BookingRequest request) {
        // Fetch the show details
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new IllegalArgumentException("Show not found"));

        // Check if the current time is after the show start time
        if (LocalDateTime.now().isAfter(ChronoLocalDateTime.from(show.getShowTime()))) {
            throw new IllegalStateException("Cannot cancel booking as the show has already started.");
        }

        // Fetch the booking using userId and showId
        Bookings booking = bookingRepository.findByUserIdAndShowId(request.getUserId(), request.getShowId())
                .orElseThrow(() -> new IllegalArgumentException("No booking found for this user and show."));

        // Retrieve seats from the booking
        List<Seats> bookedSeats = booking.getSeats();

        if (bookedSeats.isEmpty()) {
            throw new IllegalArgumentException("No seats found for cancellation.");
        }

        // Mark seats as available again
        for (Seats seat : bookedSeats) {
            seat.setIsAvailable(true);
        }

        // Save updated seats
        seatRepository.saveAll(bookedSeats);

        // Remove the booking
        bookingRepository.delete(booking);

        System.out.println("Booking canceled successfully.");
        return true;
    }



    @Override
    public List<Bookings> getBookingsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return bookingRepository.findByUser(user);
    }
}
