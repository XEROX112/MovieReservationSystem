package com.MovieReservationSystem.Response;

import com.MovieReservationSystem.Model.Role;
import com.MovieReservationSystem.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private  String token;
    private String message;
    private User user;


}
