package com.MovieReservationSystem.DTO;

import com.MovieReservationSystem.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Role role;
    private String otp;
    private String image;
    private LocalDateTime otpExpirationTime;
}
