package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Model.*;
import com.MovieReservationSystem.Repository.BookingRepository;
import com.MovieReservationSystem.Repository.SeatRepository;
import com.MovieReservationSystem.Repository.ShowRepository;
import com.MovieReservationSystem.Repository.UserRepository;
import com.MovieReservationSystem.Request.BookingRequest;
import com.MovieReservationSystem.Service.BookingService;
import com.MovieReservationSystem.Service.ShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImplementation implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final ShowService showService;

    public BookingServiceImplementation(BookingRepository bookingRepository, UserRepository userRepository,
                                        ShowRepository showRepository, SeatRepository seatRepository,
                                        ShowService showService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.showService = showService;
    }

    @Override
    @Transactional
    public Bookings bookTickets(BookingRequest request) {
        Optional<Show> showOpt = showRepository.findById(request.getShowId());
        if (showOpt.isEmpty()) {
            throw new RuntimeException("Show not found");
        }

        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        Show show = showOpt.get();

        // Handle seat lookup from frontend
        List<Seats> actualSeats = resolveSeats(request);

        // Check seat availability
        Boolean isSeatAvailable = isSeatAvailable(show.getScreen(), actualSeats);
        if (!isSeatAvailable) {
            throw new RuntimeException("Selected seats are no longer available");
        }

        // Calculate price and mark seats as booked
        Double totalBookingPrice = getPriceAndAssignSeats(show.getScreen(), actualSeats);

        Bookings bookings = new Bookings();
        bookings.setShow(show);
        bookings.setUser(user);
        bookings.setSeats(actualSeats);
        bookings.setTotalAmount(totalBookingPrice);
        bookings.setDateTime(LocalDateTime.now());

        // Save booking
        Bookings savedBooking = bookingRepository.save(bookings);

        // Update user's bookings
        user.getBookings().add(savedBooking);
        userRepository.save(user);

        // Update show's bookings
        show.getBookings().add(savedBooking);
        showRepository.save(show);

        return savedBooking;
    }

    // Method to resolve seats from frontend request
    private List<Seats> resolveSeats(BookingRequest request) {
        if (request.getRequestedSeats() != null && !request.getRequestedSeats().isEmpty()) {
            return request.getRequestedSeats().stream()
                    .map(seat -> seatRepository.findById(seat.getId())
                            .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + seat.getId())))
                    .collect(Collectors.toList());
        } else if (request.getRequestedSeats() != null && !request.getRequestedSeats().isEmpty()) {
            Optional<Show> showOpt = showRepository.findById(request.getShowId());
            if (showOpt.isEmpty()) {
                throw new RuntimeException("Show not found");
            }

            Screen screen = showOpt.get().getScreen();

            return request.getRequestedSeats().stream()
                    .map(seatNumber -> seatRepository.findBySeatRow_SeatCategory_Screen_AndSeatNumber(screen, String.valueOf(seatNumber))


                            .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNumber)))
                    .collect(Collectors.toList());

        } else {
            throw new RuntimeException("No seat information provided in booking request");
        }
    }


    // Check if all selected seats are available
    private Boolean isSeatAvailable(Screen screen, List<Seats> seats) {
        for (Seats seat : seats) {
            if (!seat.getIsAvailable()) {
                return false;
            }
            // Additional check: ensure seat belongs to the correct screen
            // Fixed: Access the screen through the relationship hierarchy
            if (!seat.getSeatRow().getSeatCategory().getScreen().getId().equals(screen.getId())) {
                throw new RuntimeException("Seat does not belong to the specified screen");
            }
        }
        return true;
    }

    // Calculate total price and mark seats as booked
    public Double getPriceAndAssignSeats(Screen screen, List<Seats> seats) {
        Double totalPrice = 0.0;

        for (Seats seat : seats) {
            // Get price from seat category instead of seat directly
            // Since Seats model doesn't have price field, get it from SeatCategory
            Double seatPrice = (double) seat.getSeatRow().getSeatCategory().getPrice();
            totalPrice += seatPrice;

            // Mark seat as unavailable
            seat.setIsAvailable(false);
            seatRepository.save(seat);
        }

        return totalPrice;
    }

    @Override
    public List<Bookings> getBookingsByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return bookingRepository.findByUser(userOpt.get());
    }

    @Override
    public Optional<Bookings> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Optional<Bookings> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }

        Bookings booking = bookingOpt.get();

        // Check if booking can be cancelled (e.g., show hasn't started)


        // Release the seats
        for (Seats seat : booking.getSeats()) {
            seat.setIsAvailable(true);
            seatRepository.save(seat);
        }

        // Remove booking from user and show
        booking.getUser().getBookings().remove(booking);
        booking.getShow().getBookings().remove(booking);

        // Delete the booking
        bookingRepository.delete(booking);
    }

    @Override
    public List<Bookings> getAllBookings() {
        return bookingRepository.findAll();
    }
}