package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.APIResponse;
import com.MovieReservationSystem.DTO.UpdatedUser;
import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.User;
import com.MovieReservationSystem.Request.ChangePasswordRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public User updateUser(Long id, UpdatedUser updatedUser) throws Exception;
    public List<Bookings>getBookings(Long id);
    public  void deleteUser(Long id);
    public User getUserByEmail(String email);
    public boolean verifyOtpAndChangeEmail(Long id, String enteredOtp, String newEmail);
    public APIResponse changePassword(ChangePasswordRequest request) throws Exception;
    public  String uploadImage(MultipartFile file,String token) throws Exception;
    public boolean generateAndSendOtp(Long id, String newEmail);
}
