package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.APIResponse;
import com.MovieReservationSystem.DTO.UpdatedUser;
import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.User;
import com.MovieReservationSystem.Request.ChangePasswordRequest;
import com.MovieReservationSystem.Request.UserRequest;
import com.MovieReservationSystem.Response.UserBookingResponse;
import com.MovieReservationSystem.Response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public User updateUser(Long id, UpdatedUser updatedUser) throws Exception;

    public List<UserBookingResponse> getBookings(Long id);

    public void deleteUser(Long id);

    public UserResponse getUserByEmail(String email, Long id);

    public boolean verifyOtpAndChangeEmail(Long id, String enteredOtp, String newEmail);

    public APIResponse changePassword(ChangePasswordRequest request) throws Exception;

    public String uploadImage(MultipartFile file, String token) throws Exception;

    public boolean generateAndSendOtp(Long id, String newEmail);

    User addUser(@Valid UserRequest request, Long id);

    String getProfileImage(Long userId);
}
