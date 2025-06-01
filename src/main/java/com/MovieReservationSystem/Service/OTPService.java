package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.User;
import com.MovieReservationSystem.Repository.UserRepository;
import com.MovieReservationSystem.Request.VerifyOtpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OTPService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    public OTPService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }
    public  String generateOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
    public void sendOTPToEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("jayabhardwaz1@gmail.com");
        mailSender.send(message);
    }

    public String verifyOtp(String email, String otp) {
        // Step 1: Fetch the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Step 2: Check if OTP exists and matches
        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // Step 3: Check if OTP has expired
        if (user.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        // Step 4: OTP is valid, clear OTP and return success message
        user.setOtp(null); // Clear OTP
        user.setOtpExpirationTime(null); // Clear expiration time
        userRepository.save(user); // Save the user

        return "OK";
    }
}
