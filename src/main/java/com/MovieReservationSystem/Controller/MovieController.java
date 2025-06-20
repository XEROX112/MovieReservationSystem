package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.DTO.MovieDTO;
import com.MovieReservationSystem.Mapper.MovieMapper;
import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Response.MovieDetailsDto;
import com.MovieReservationSystem.Response.MovieResponse;
import com.MovieReservationSystem.Response.MovieResponseDto;
import com.MovieReservationSystem.Service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<MovieResponseDto>> getMoviesByRegion(@PathVariable String region) {
        System.out.println("hi");
        List<Movie> movies = movieService.getMoviesByRegion(region);
        List<MovieResponseDto> dtos = movies.stream()
                .map(MovieMapper::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/admin/add/{id}")
    public ResponseEntity<String> addMovie(@RequestBody MovieDTO movie, @PathVariable("id") Long id) {
        Movie newMovie = movieService.addMovie(movie, id);
        if (newMovie != null) {
            return ResponseEntity.ok(newMovie.getTitle());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateMovie(@RequestBody Movie movie) {
        Movie newMovie = movieService.updateMovie(movie);
        return ResponseEntity.ok(newMovie.getTitle());
    }


    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> removeMovie(@PathVariable("id") Long id) {
        String msg = movieService.deleteMovie(id);
        if (msg.equals("Movie deleted successfully")) {
            return ResponseEntity.ok(msg);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<List<MovieResponse>> getMoviesByTheaterId(@PathVariable Long id) {
        List<MovieResponse> movies = movieService.getAllMoviesByTheaterId(id);
        if (movies == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/get-movie/{id}")
    public ResponseEntity<MovieDetailsDto> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }

        MovieDetailsDto dto = new MovieDetailsDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setCast(movie.getCast());
        dto.setDescription(movie.getDescription());
        dto.setAbout(movie.getAbout());
        dto.setPoster(movie.getPoster());
        dto.setDuration(movie.getDuration());
        dto.setLanguage(movie.getLanguage());
        dto.setFormat(movie.getFormat());
        return ResponseEntity.ok(dto);
    }

}
