package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.DTO.AddTheatre;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class TheatreController {
    private final TheaterService theaterService;

    public TheatreController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping("/add-theatre")
    public ResponseEntity<String> addTheatre(@Valid @RequestBody Theatre addTheatre) {
        Theatre theatre = theaterService.addTheater(addTheatre);
        return ResponseEntity.status(HttpStatus.CREATED).body("Theatre Added");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheatre(@PathVariable Long id) {
        boolean deleted = theaterService.deleteTheatre(id);

        if (deleted) {
            return ResponseEntity.ok("Theatre deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Theatre not found.");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Theatre> updateTheatre(@PathVariable Long id, @Valid @RequestBody Theatre updatedTheatre) {
        Theatre theatre = theaterService.updateTheatre(id, updatedTheatre);
        return ResponseEntity.ok(theatre);
    }


    @GetMapping("/{movieTitle}/region/{region}")
    public ResponseEntity<List<Theatre>> getTheatresByMovieAndRegion(@PathVariable String movieTitle,
                                                                     @PathVariable String region) {
        List<Theatre> theatres = theaterService.getTheatresByMovieAndRegion(movieTitle, region);
        return ResponseEntity.ok(theatres);
    }
}
