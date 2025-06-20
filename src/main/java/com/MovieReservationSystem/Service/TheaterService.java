package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.AddTheatreRequest;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Response.TheaterMovieResponse;

import java.util.List;

public interface TheaterService {
    Theatre addTheater(AddTheatreRequest theatre);

    List<Theatre> getAllTheaters();

    public List<Theatre> getTheaterByUserId(Long userId);

    boolean deleteTheatre(Long id);

    Theatre updateTheatre(Long id, AddTheatreRequest updatedTheatre);

    long countTheatres();

    List<Theatre> searchTheatreByName(String name);

    boolean doesTheatreExist(String name);

    public List<Theatre> getTheatresByMovieAndRegion(String movieTitle, String region);

    List<TheaterMovieResponse> getResponseByUser(Long userId);
}
