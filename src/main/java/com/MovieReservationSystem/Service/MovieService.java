package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.MovieDTO;
import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Response.MovieResponse;

import java.util.List;

public interface MovieService {
    public Movie addMovie(MovieDTO movie, Long theaterId);

    public Movie getMovieByName(String name);

    public Movie updateMovie(Movie movie);

    public String deleteMovie(Long id);

    public List<Movie> getMoviesByRegion(String region);

    public List<MovieResponse> getAllMoviesByTheaterId(Long theaterId);

    public Movie getMovieById(Long id);
}
