package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.DTO.APIResponse;
import com.MovieReservationSystem.Model.User;
import com.MovieReservationSystem.Request.ChangePasswordRequest;
import com.MovieReservationSystem.Request.ForgetPasswordRequest;
import com.MovieReservationSystem.Request.UserRequest;
import com.MovieReservationSystem.Request.VerifyOtpRequest;
import com.MovieReservationSystem.Response.UserBookingResponse;
import com.MovieReservationSystem.Response.UserResponse;
import com.MovieReservationSystem.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/add/{id}")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserRequest request, @PathVariable Long id) {
        User response = userService.addUser(request, id);
        return ResponseEntity.ok("User added successfully: " + response.getEmail());
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestHeader("X-User-Email") String email, @PathVariable Long id) {
        UserResponse response = userService.getUserByEmail(email, id);
        return ResponseEntity.ok(response);
    }


    // Get user bookings
    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<UserBookingResponse>> getUserBookings(@PathVariable Long id) {
        List<UserBookingResponse> userBookings = userService.getBookings(id);
        if (userBookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userBookings);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    // Verify and change email
    @PostMapping("/{id}/request-email-change")
    public ResponseEntity<String> requestEmailChange(@PathVariable Long id, @RequestBody ForgetPasswordRequest request) {
        boolean isOtpSent = userService.generateAndSendOtp(id, request.getEmail());
        return isOtpSent
                ? ResponseEntity.ok("OTP sent to " + request.getEmail() + ". Please verify within 5 minutes.")
                : ResponseEntity.badRequest().body("Failed to send OTP.");
    }

    @PostMapping("/{id}/verify-email-change-verify")
    public ResponseEntity<String> verifyEmailChange(@PathVariable Long id, @RequestBody VerifyOtpRequest request, @RequestHeader("X-User-Email") String newEmail) {
        boolean isVerified = userService.verifyOtpAndChangeEmail(id, request.getOtp(), newEmail);
        return isVerified
                ? ResponseEntity.ok("Email verified and updated.")
                : ResponseEntity.badRequest().body("Invalid or expired OTP.");
    }


    @PostMapping("/change-password")
    public ResponseEntity<APIResponse> changePassword(@RequestBody ChangePasswordRequest request) throws Exception {
        APIResponse response = userService.changePassword(request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        try {
            String imageUrl = userService.uploadImage(file, token);
            return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<String> getUserProfileImage(@PathVariable Long userId) {
        String img = userService.getProfileImage(userId);
        if (img != null) return ResponseEntity.ok(img);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
