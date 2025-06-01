package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Configuration.JwtProvider;
import com.MovieReservationSystem.DTO.APIResponse;
import com.MovieReservationSystem.DTO.UpdatedUser;
import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.User;
import com.MovieReservationSystem.Repository.BookingRepository;
import com.MovieReservationSystem.Repository.UserRepository;
import com.MovieReservationSystem.Request.ChangePasswordRequest;
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
    private  final CloudinaryService cloudinaryService;
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
    public List<Bookings> getBookings(Long id) {
        User user = userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public String uploadImage(MultipartFile file,String token) throws Exception {
       String imageUrl   = cloudinaryService.uploadImage(file);
       String email=jwtProvider.getEmailFromToken(token);
       User user = userRepository.findByEmail(email);
       user.setImage(imageUrl);
       userRepository.save(user);
       return imageUrl;
    }

}
