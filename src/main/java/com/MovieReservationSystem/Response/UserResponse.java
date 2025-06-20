package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean married;
    private String image;
    private String gender;
    private Date dateOfBirth;
    private String PhoneNumber;
    private String email;
    private String Pincode;
    private String Address;
    private String Landmark;
    private String City;
    private String State;
}
