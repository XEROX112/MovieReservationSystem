package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.CastDTO;
import com.MovieReservationSystem.DTO.MovieDTO;
import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Model.professionalIdentity;
import com.MovieReservationSystem.Repository.MovieRepository;
import com.MovieReservationSystem.Service.CloudinaryService;
import com.MovieReservationSystem.Service.MovieService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImplementation implements MovieService {
    private final MovieRepository movieRepository;
    private final CloudinaryService cloudinaryService;

    public MovieServiceImplementation(MovieRepository movieRepository, CloudinaryService cloudinaryService) {
        this.movieRepository = movieRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public Movie addMovie(MovieDTO movie) {
        Movie existingMovie = movieRepository.findByTitle(movie.getTitle());
        if (existingMovie != null) {
            return existingMovie;
        }
        Movie newMovie = new Movie();
        newMovie.setTitle(movie.getTitle());
        newMovie.setAbout(movie.getAbout());
        newMovie.setReleaseDate(movie.getYear());
        newMovie.setDuration(movie.getDuration());
        newMovie.setGenre(movie.getGenre());
        newMovie.setDescription(movie.getDescription());
        newMovie.setLanguage(movie.getLanguage());
        newMovie.setCertification(movie.getCertification());
        newMovie.setPoster(movie.getPoster());
        newMovie.setFormat(movie.getFormats());
        List<professionalIdentity> castList = new ArrayList<>();
        for (CastDTO cast : movie.getCast()) {
            professionalIdentity identity = new professionalIdentity();
            identity.setName(cast.getName());
            identity.setRole(cast.getRole());
            identity.setPhoto(cast.getImage());
            castList.add(identity);
        }
        newMovie.setCast(castList);
        return movieRepository.save(newMovie);
    }

    
    @Override
    public Movie getMovieByName(String name) {
        return movieRepository.findByTitle(name);
    }

    @Override
    public Movie updateMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(String title) {
        Movie movie = movieRepository.findByTitle(title);
        movieRepository.deleteById(movie.getId());
    }

    @Override
    public List<Movie> getMoviesByRegion(String region) {
        return movieRepository.findMoviesByRegion(region);
    }
}
