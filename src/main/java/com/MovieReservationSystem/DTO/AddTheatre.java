package com.MovieReservationSystem.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTheatre {
    @NotBlank(message = "Theater name cannot be empty")
    private String   theaterName ;

    @NotBlank(message = "Region cannot be empty")
    private String region;
}
