package com.MovieReservationSystem.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowUpdateDTO {
    private Long id;
    private String time;
    private String language;
    private String format;
    private String screen;
}
