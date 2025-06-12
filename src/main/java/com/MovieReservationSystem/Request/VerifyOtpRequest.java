package com.MovieReservationSystem.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyOtpRequest {

    @NotBlank
    private String OTP;
}
