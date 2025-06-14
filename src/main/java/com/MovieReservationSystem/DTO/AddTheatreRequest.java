package com.MovieReservationSystem.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddTheatreRequest {
    @NotBlank(message = "Theater name cannot be empty")
    private String theaterName;

    @NotBlank(message = "Region cannot be empty")
    private String region;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotEmpty(message = "Screens cannot be empty")
    private List<ScreenRequest> screens;
}

