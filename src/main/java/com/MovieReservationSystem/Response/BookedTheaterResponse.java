package com.MovieReservationSystem.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookedTheaterResponse {
    private String name;
    private String address;
    private String region;
}
