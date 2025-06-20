package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Configuration.JwtProvider;
import com.MovieReservationSystem.DTO.APIResponse;
import com.MovieReservationSystem.DTO.UpdatedUser;
import com.MovieReservationSystem.Model.*;
import com.MovieReservationSystem.Repository.BookingRepository;
import com.MovieReservationSystem.Repository.UserRepository;
import com.MovieReservationSystem.Request.ChangePasswordRequest;
import com.MovieReservationSystem.Request.UserRequest;
import com.MovieReservationSystem.Response.*;
import com.MovieReservationSystem.Service.CloudinaryService;
import com.MovieReservationSystem.Service.OTPService;
import com.MovieReservationSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final OTPService otpService;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final JwtProvider jwtProvider;
    private final BookingRepository bookingRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, OTPService otpService, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService, JwtProvider jwtProvider, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.jwtProvider = jwtProvider;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public User updateUser(Long id, UpdatedUser updatedUser) {
        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals("null")) {
            user.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getPhoneNumber() != null && !updatedUser.getPhoneNumber().equals("null")) {
            user.setPhone(updatedUser.getPhoneNumber());
        }

        return userRepository.save(user);
    }

    @Override
    public List<UserBookingResponse> getBookings(Long id) {
        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Bookings> bookings = bookingRepository.findByUser(user);

        return bookings.stream().map(booking -> {
            UserBookingResponse response = new UserBookingResponse();
            response.setId(booking.getId());
            response.setDateTime(booking.getDateTime());
            response.setTotalAmount(booking.getTotalAmount());

            // Show
            Show show = booking.getShow();
            BookedShowResponse showResponse = new BookedShowResponse();
            showResponse.setId(show.getId());
            showResponse.setShowDate(show.getShowDate().toString());
            showResponse.setShowTime(show.getShowTime());

            // Movie
            Movie movie = show.getMovie();
            BookedMovieResponse movieResponse = new BookedMovieResponse();
            movieResponse.setTitle(movie.getTitle());
            movieResponse.setPoster(movie.getPoster());
            showResponse.setMovie(movieResponse);

            // Theater
            Theatre theater = show.getTheater();
            BookedTheaterResponse theaterResponse = new BookedTheaterResponse();
            theaterResponse.setName(theater.getTheaterName());
            theaterResponse.setAddress(theater.getAddress());
            theaterResponse.setRegion(theater.getRegion());
            showResponse.setTheater(theaterResponse);

            response.setShow(showResponse);

            // Seats
            List<BookedSeatResponse> seatResponses = booking.getSeats().stream().map(seat -> {
                BookedSeatResponse sr = new BookedSeatResponse();
                sr.setSeatNumber(seat.getSeatNumber());
                return sr;
            }).toList();

            response.setSeats(seatResponses);

            return response;
        }).toList();
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserByEmail(String email, Long id) {
        UserResponse userResponse = new UserResponse();
        User user = userRepository.findById(id).get();

        if (user == null) return userResponse;

        userResponse.setId(user.getId());

        userResponse.setFirstName(user.getFirstName() != null ? user.getFirstName() : null);
        userResponse.setLastName(user.getLastName() != null ? user.getLastName() : null);
        userResponse.setEmail(user.getEmail() != null ? user.getEmail() : null);
        userResponse.setPhoneNumber(user.getPhone() != null ? user.getPhone() : null);
        userResponse.setGender(user.getGender() != null ? user.getGender() : null);
        userResponse.setMarried(user.isMarried()); // boolean — always has a default (false)
        userResponse.setDateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth() : null);
        userResponse.setUsername(user.getUsername() != null ? user.getUsername() : null);
        userResponse.setImage(user.getImage() != null ? user.getImage() : null);
        Address address = user.getAddress();
        if (address != null) {
            userResponse.setAddress(address.getPlace() != null ? address.getPlace() : null);
            userResponse.setPincode(address.getPincode() != null ? address.getPincode() : null);
            userResponse.setLandmark(address.getLandmark() != null ? address.getLandmark() : null);
            userResponse.setCity(address.getCity() != null ? address.getCity() : null);
            userResponse.setState(address.getState() != null ? address.getState() : null);
        } else {
            userResponse.setAddress(null);
            userResponse.setPincode(null);
            userResponse.setLandmark(null);
            userResponse.setCity(null);
            userResponse.setState(null);
        }

        return userResponse;
    }


    @Override
    public boolean generateAndSendOtp(Long id, String newEmail) {
        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        String OTP = otpService.generateOtp(6);
        user.setOtp(OTP);
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);  // Save OTP in database

        String message = String.format("Your OTP is %s. Please verify within 5 minutes.", OTP);
        otpService.sendOTPToEmail(newEmail, "Email Verification", message);

        return true;
    }

    @Override
    public User addUser(UserRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = user.getAddress();
        if (address == null) address = new Address();

        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            address.setPlace(request.getAddress());
        }

        if (request.getCity() != null && !request.getCity().isBlank()) {
            address.setCity(request.getCity());
        }

        if (request.getState() != null && !request.getState().isBlank()) {
            address.setState(request.getState());
        }

        if (request.getLandmark() != null && !request.getLandmark().isBlank()) {
            address.setLandmark(request.getLandmark());
        }

        if (request.getPincode() != null && !request.getPincode().isBlank()) {
            address.setPincode(request.getPincode());
        }

        user.setAddress(address);

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhone(request.getPhoneNumber());
        }

        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }

        if (request.getGender() != null && !request.getGender().isBlank()) {
            user.setGender(request.getGender());
        }

        user.setMarried(request.isMarried()); // boolean always has a value

        System.out.println("Before save: " + user);
        User savedUser = userRepository.save(user);
        System.out.println("After save: " + savedUser);
        return savedUser;
    }

    @Override
    public String getProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String img = user.getImage() != null ? user.getImage() : null;
        return img;
    }


    @Override
    public boolean verifyOtpAndChangeEmail(Long id, String enteredOtp, String newEmail) {
        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if OTP is correct and not expired
        if (user.getOtp() == null || !user.getOtp().equals(enteredOtp) || user.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            return false;  // Invalid OTP
        }

        // OTP is correct → Update email
        user.setEmail(newEmail);
        user.setOtp(null);  // Clear OTP after successful verification
        user.setOtpExpirationTime(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public APIResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(changePasswordRequest.getEmail());

        if (user == null) {
            return new APIResponse(false, "User not found");
        }

        // Validate the old password
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return new APIResponse(false, "Old password is incorrect");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            return new APIResponse(false, "New password and confirm password do not match");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return new APIResponse(true, "Password changed successfully");
    }

    @Override
    public String uploadImage(MultipartFile file, String token) throws Exception {
        String imageUrl = cloudinaryService.uploadImage(file);
        String email = jwtProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);
        user.setImage(imageUrl);
        userRepository.save(user);
        return imageUrl;
    }

}
