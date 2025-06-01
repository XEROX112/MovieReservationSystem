package com.MovieReservationSystem.Request;

import com.MovieReservationSystem.Model.Movie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddShowRequest {
    @NotNull(message = "Showtime cannot be null")
    private LocalTime showtime;  // ✅ Use @NotNull for non-string fields

    @NotNull(message = "Show date cannot be null")
    private LocalDate showDate;  // ✅ Use @NotNull instead of @NotBlank

    @NotNull(message = "Movie cannot be null")
    private Movie movie;  // ✅ Use @NotNull since this is an object

    @NotBlank(message = "Screen number cannot be blank")
    private String screenNo;  // ✅ @NotBlank is fine for Strings
}


