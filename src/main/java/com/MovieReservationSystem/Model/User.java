package com.MovieReservationSystem.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private  Role role;
    private String otp;
    private String image;
    private LocalDateTime otpExpirationTime;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bookings>bookings;
}
