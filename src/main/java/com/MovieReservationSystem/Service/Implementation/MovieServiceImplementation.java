package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Repository.MovieRepository;
import com.MovieReservationSystem.Service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImplementation implements MovieService {
    private final MovieRepository movieRepository;

    public MovieServiceImplementation(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie addMovie(Movie movie) {
        if (movieRepository.existsByTitle(movie.getTitle())) {
            return movieRepository.save(movie);
        }
       return null;
    }


    @Override
    public Movie getMovieByName(String name) {
        return  movieRepository.findByTitle(name);
    }

    @Override
    public Movie updateMovie(Movie movie) {
        return  movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(String title) {
            movieRepository.deleteById(movieId);
    }

    @Override
    public List<Movie> getMoviesByRegion(String region) {
        return movieRepository.findMoviesByRegion(region);
    }
}
