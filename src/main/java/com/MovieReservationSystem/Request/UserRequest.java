package com.MovieReservationSystem.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private boolean married;
    private String gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String pincode;
    private String address;
    private String landmark;
    private String city;
    private String state;
}
