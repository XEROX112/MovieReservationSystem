package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.Movie;

import java.util.List;

public interface MovieService {
    public Movie addMovie(Movie movie);
    public Movie getMovieByName(String name);
    public Movie updateMovie(Movie movie);
    public void deleteMovie(String title);
    public List<Movie> getMoviesByRegion(String region);

}
