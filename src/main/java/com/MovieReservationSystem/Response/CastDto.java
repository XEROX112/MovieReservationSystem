package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CastDto {
    private String name;
    private String role;
    private String image;
}
