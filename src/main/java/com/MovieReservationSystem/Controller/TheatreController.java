package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.DTO.AddTheatreRequest;
import com.MovieReservationSystem.Mapper.TheatreMapper;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Response.TheaterMovieResponse;
import com.MovieReservationSystem.Response.TheatreResponse;
import com.MovieReservationSystem.Service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/theater")
public class TheatreController {
    private final TheaterService theaterService;

    public TheatreController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping("/admin/add-theatre")
    public ResponseEntity<Theatre> addTheatre(@Valid @RequestBody AddTheatreRequest addTheatre) {
        Theatre theatre = theaterService.addTheater(addTheatre);
        return ResponseEntity.status(HttpStatus.CREATED).body(theatre);
    }

    @GetMapping("/theatres/{userid}")
    public ResponseEntity<List<TheatreResponse>> getTheatreByUserId(@PathVariable Long userid) {
        List<Theatre> theatres = theaterService.getTheaterByUserId(userid);
        List<TheatreResponse> responseList = theatres.stream()
                .map(TheatreMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
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

    @GetMapping("/all-region")
    public ResponseEntity<List<String>> getAllRegions() {
        List<Theatre> theaters = theaterService.getAllTheaters();
        List<String> regionList = theaters.stream().map(Theatre::getRegion).distinct().toList();
        if (regionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(regionList);
    }

    @GetMapping("/admin/{userId}/theaters")
    public List<TheaterMovieResponse> getTheatersByUserId(@PathVariable Long userId) {
        List<TheaterMovieResponse> responses = theaterService.getResponseByUser(userId);
        if (responses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No theaters found for user with id: " + userId);
        }
        return responses;
    }
}
