package com.MovieReservationSystem.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddShowRequest {
    private String showtime;
    private String showDate;

    // Use Long movieId instead of Movie object to avoid circular reference
    @NotNull(message = "Movie ID cannot be null")
    private Long movieId;

    @NotBlank(message = "Screen number cannot be blank")
    private String screenNo;

    private String language;
    private String format;
}