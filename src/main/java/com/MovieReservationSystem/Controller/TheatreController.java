package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.DTO.AddTheatreRequest;
import com.MovieReservationSystem.Mapper.TheatreMapper;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Response.TheatreResponse;
import com.MovieReservationSystem.Service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Theatre> addTheatre(@Valid @RequestBody AddTheatreRequest addTheatre) {
        Theatre theatre = theaterService.addTheater(addTheatre);
        return ResponseEntity.status(HttpStatus.CREATED).body(theatre);
    }

    @GetMapping("/theatres/{id}")
    public ResponseEntity<TheatreResponse> getTheatreById(@PathVariable Long id) {
        Theatre theatre = theaterService.getTheaterById(id);
        TheatreResponse response = TheatreMapper.toDto(theatre);
        return ResponseEntity.ok(response);
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


    @PutMapping("/theatres/{id}")
    public ResponseEntity<Theatre> updateTheatre(@Valid @RequestBody AddTheatreRequest updatedTheatre, @PathVariable Long id) {
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
