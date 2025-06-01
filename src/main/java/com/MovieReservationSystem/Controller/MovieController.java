package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/region/{region}")
    public ResponseEntity<List<Movie>> getMoviesByRegion(@PathVariable String region) {
        List<Movie> movies = movieService.getMoviesByRegion(region);
        return ResponseEntity.ok(movies);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie) {
        Movie newMovie=movieService.addMovie(movie);
        if(newMovie!=null) {
            return ResponseEntity.ok(newMovie.getTitle());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateMovie(@RequestBody Movie movie) {
        Movie newMovie=movieService.updateMovie(movie);
        return ResponseEntity.ok(newMovie.getTitle());
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> removeMovie(@RequestParam String title) {
        Movie newMovie=movieService.addMovie(movie);
        if(newMovie!=null) {
            return ResponseEntity.ok(newMovie.getTitle());
        }
        return ResponseEntity.notFound().build();
    }

}

