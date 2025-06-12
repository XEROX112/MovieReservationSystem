package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.Configuration.JwtProvider;
import com.MovieReservationSystem.Model.Role;
import com.MovieReservationSystem.Model.User;
import com.MovieReservationSystem.Repository.UserRepository;
import com.MovieReservationSystem.Request.*;
import com.MovieReservationSystem.Response.AuthResponse;
import com.MovieReservationSystem.Service.CustomUserDetailService;
import com.MovieReservationSystem.Service.OTPService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailService customUserDetails;
    private final OTPService otpService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider,
                          CustomUserDetailService customUserDetails, OTPService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserDetails = customUserDetails;
        this.otpService = otpService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> initiateSignup(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        if (userRepository.findByEmail(authRequest.getEmail()) != null) {
            throw new Exception("Email Already Exists");
        }

        if (!Objects.equals(authRequest.getPassword(), authRequest.getConfirmPassword())) {
            throw new Exception("Passwords do not match. Please try again.");
        }

        if (!isValidRole(authRequest.getRole())) {
            throw new Exception("Invalid role provided. Please choose a valid role.");
        }

        String otp = otpService.generateOtp(6);
        LocalDateTime otpExpirationTime = LocalDateTime.now().plusMinutes(5);

        User user = new User();
        user.setUsername(authRequest.getFullName());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.valueOf(authRequest.getRole().toUpperCase()));
        user.setOtp(otp);
        user.setOtpExpirationTime(otpExpirationTime);

        userRepository.save(user);

        String message = String.format("Your One-Time Password (OTP) is %s. Please use this code to complete your verification. The OTP is valid for 10 minutes.", otp);
        otpService.sendOTPToEmail(user.getEmail(), "Verification Code", message);

        return new ResponseEntity<>("OTP sent to email. Please verify to complete signup.", HttpStatus.OK);
    }

    @PostMapping("/verify-Signotp")
    public ResponseEntity<String> verifySignOtp(@RequestBody VerifyOtpRequest request, @RequestHeader("email") String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found. Please sign up again.");
        }

        String message = otpService.verifyOtp(user.getEmail(), request.getOTP());

        if ("OTP has expired".equals(message) || ("Invalid OTP".equals(message) && LocalDateTime.now().isAfter(user.getOtpExpirationTime()))) {
            userRepository.delete(user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid OTP. Please sign up again.");
        }

       return new ResponseEntity<>("OTP Verified", HttpStatus.CREATED);
    }

    @PostMapping("/resend-signup-otp")
    public ResponseEntity<String> resendSignupOtp(@RequestBody VerifyOtpRequest request,@RequestHeader("email") String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found. Please sign up again.");
        }

        String newOtp = otpService.generateOtp(6);
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        user.setOtp(newOtp);
        user.setOtpExpirationTime(expirationTime);
        userRepository.save(user);

        String msg = String.format("Your new OTP is %s. It's valid for 5 minutes.", newOtp);
        otpService.sendOTPToEmail(email, "Resend OTP", msg);

        String message = otpService.verifyOtp(user.getEmail(),request.getOTP());
        if ("OTP has expired".equals(message) || ("Invalid OTP".equals(message) && LocalDateTime.now().isAfter(user.getOtpExpirationTime()))) {
            userRepository.delete(user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid OTP. Please sign up again.");
        }

        return new ResponseEntity<>("OTP Verified", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody LoginRequest request) throws Exception {
        Authentication authentication = authenticateUser(request.getEmail(), request.getPassword());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String token = jwtProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("Successfully logged in");
        authResponse.setUser(user);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticateUser(String userName, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(userName);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Email");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userName, password, userDetails.getAuthorities());
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> requestOtp(@RequestBody ForgetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Email not found!"));
        }

        String otp = otpService.generateOtp(6);
        user.setOtp(otp);
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        otpService.sendOTPToEmail(user.getEmail(), "Verification Code", "Your OTP is " + otp);

        return ResponseEntity.ok(Collections.singletonMap("redirectUrl", "/auth/verify-otp"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request, @RequestHeader("email") String email) {
        try {
            otpService.verifyOtp(email, request.getOTP());
            return ResponseEntity.ok(Collections.singletonMap("redirectUrl", "/auth/reset-password"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "OTP is not found!"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, @RequestHeader("email") String email) {
        User user = userRepository.findByEmail(email);
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Passwords do not match!"));
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpirationTime(null);
        userRepository.save(user);

        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }

    private boolean isValidRole(String role) {
        try {
            Role.valueOf(role.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}