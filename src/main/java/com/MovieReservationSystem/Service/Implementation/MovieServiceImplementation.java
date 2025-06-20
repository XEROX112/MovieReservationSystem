package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.CastDTO;
import com.MovieReservationSystem.DTO.MovieDTO;
import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Model.professionalIdentity;
import com.MovieReservationSystem.Repository.MovieRepository;
import com.MovieReservationSystem.Repository.TheatreRepository;
import com.MovieReservationSystem.Response.MovieResponse;
import com.MovieReservationSystem.Service.CloudinaryService;
import com.MovieReservationSystem.Service.MovieService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImplementation implements MovieService {
    private final MovieRepository movieRepository;
    private final CloudinaryService cloudinaryService;
    private final TheatreRepository theatreRepository;

    public MovieServiceImplementation(MovieRepository movieRepository, CloudinaryService cloudinaryService, TheatreRepository theatreRepository) {
        this.movieRepository = movieRepository;
        this.cloudinaryService = cloudinaryService;
        this.theatreRepository = theatreRepository;
    }

    @Override
    public Movie addMovie(MovieDTO movie, Long theaterId) {
        Theatre theatre = theatreRepository.findById(theaterId).get();
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
        newMovie.setTheatre(theatre);
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
    public String deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).get();
        if (movie == null) {
            return "Movie not found with ID: " + id;
        }
        movieRepository.deleteById(movie.getId());
        return "Movie deleted successfully";
    }

    @Override
    public List<Movie> getMoviesByRegion(String region) {
        return movieRepository.findMoviesByRegion(region);
    }


    @Override
    public List<MovieResponse> getAllMoviesByTheaterId(Long theaterId) {
        Theatre theatre = theatreRepository.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theatre not found with ID: " + theaterId));

        List<Movie> savedMovies = movieRepository.findMoviesByTheatre_Id(theaterId);
        int screenCount = theatre.getScreens().size();

        return savedMovies.stream()
                .map(movie -> {
                    MovieResponse response = new MovieResponse();
                    response.setId(movie.getId());
                    response.setTitle(movie.getTitle());
                    response.setDuration(movie.getDuration());
                    response.setGenre(movie.getGenre());
                    response.setPoster(movie.getPoster());
                    response.setTotalScreen(screenCount);
                    response.setLanguage(movie.getLanguage());
                    response.setFormat(movie.getFormat());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Movie getMovieById(Long id) {
        System.out.println(movieRepository.findById(id));
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
    }
}
