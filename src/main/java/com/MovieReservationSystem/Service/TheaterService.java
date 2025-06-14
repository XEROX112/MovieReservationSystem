package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.AddTheatreRequest;
import com.MovieReservationSystem.Model.Theatre;

import java.util.List;

public interface TheaterService {
    Theatre addTheater(AddTheatreRequest theatre);

    List<Theatre> getAllTheaters();

    Theatre getTheaterById(Long theaterId);

    boolean deleteTheatre(Long id);

    Theatre updateTheatre(Long id, AddTheatreRequest updatedTheatre);

    long countTheatres();

    List<Theatre> searchTheatreByName(String name);

    boolean doesTheatreExist(String name);

    public List<Theatre> getTheatresByMovieAndRegion(String movieTitle, String region);

}
