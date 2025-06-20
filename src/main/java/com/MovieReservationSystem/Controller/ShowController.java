package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.DTO.FormatSelectionDto;
import com.MovieReservationSystem.DTO.MovieShowsDTO;
import com.MovieReservationSystem.DTO.ShowDetailsDTO;
import com.MovieReservationSystem.DTO.ShowUpdateDTO;
import com.MovieReservationSystem.Model.Show;
import com.MovieReservationSystem.Model.Seats;
import com.MovieReservationSystem.Request.AddShowRequest;
import com.MovieReservationSystem.Response.DeleteShowMessage;
import com.MovieReservationSystem.Response.SeatDto;
import com.MovieReservationSystem.Response.TheaterShowResponseDTO;
import com.MovieReservationSystem.Service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shows")
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping(value = "/admin/add/{id}", consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<?> addShow(
            @PathVariable Long id,
            @RequestBody AddShowRequest addShow) {

        try {
            Show show = showService.addShow(id, addShow);
            return ResponseEntity.ok(show);
        } catch (RuntimeException e) {
            // Return proper error response
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/admin/update/{theatreId}")
    public ResponseEntity<?> updateShows(@PathVariable Long theatreId,
                                         @RequestBody Map<Long, List<ShowUpdateDTO>> movieShowMap) {
        try {
            List<Show> updated = new ArrayList<>();
            for (Map.Entry<Long, List<ShowUpdateDTO>> entry : movieShowMap.entrySet()) {
                Long movieId = entry.getKey();
                for (ShowUpdateDTO dto : entry.getValue()) {
                    Show updatedShow = showService.updateShow(movieId, dto, theatreId);
                    updated.add(updatedShow);
                }
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteShowMessage> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        DeleteShowMessage message = new DeleteShowMessage();
        message.setMessage("Show deleted successfully");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/admin/theatre/{id}")
    public ResponseEntity<List<MovieShowsDTO>> getShowsByTheater(@PathVariable Long id) {
        List<MovieShowsDTO> shows = showService.getShowsByTheater(id);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/format/{region}/{id}")
    public ResponseEntity<FormatSelectionDto> getFormatSelection(@PathVariable String region, @PathVariable Long id) {
        FormatSelectionDto dto = showService.getFormatAndLanguage(region, id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{region}/theaters")
    public ResponseEntity<List<TheaterShowResponseDTO>> getShowsByMovieIdAndLangAndFormat(@RequestParam Long movieId, @RequestParam String lang,
                                                                                          @RequestParam String format, @PathVariable String region,
                                                                                          @RequestHeader("time") String showDate) {
        List<TheaterShowResponseDTO> dtos = showService.getShowsByMovieIdAndLangAndFormat(movieId, lang, format, region, showDate);
        if (dtos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDetailsDTO> getShowById(@PathVariable Long id) {
        try {
            ShowDetailsDTO showDetails = showService.getShowDetailsForFrontend(id);
            return ResponseEntity.ok(showDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{showId}/booked-seats")
    public ResponseEntity<List<String>> getBookedSeats(@PathVariable Long showId) {
        try {
            List<String> bookedSeats = showService.getBookedSeatsForShow(showId);
            return ResponseEntity.ok(bookedSeats);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>()); // Return empty list if error
        }
    }

    // New endpoint to find seat by seat number and show ID
    @GetMapping("/{showId}/seats/find")
    public ResponseEntity<SeatDto> findSeatBySeatNumber(
            @PathVariable Long showId,
            @RequestParam String seatNumber) {
        try {
            SeatDto seat = showService.findSeatBySeatNumberAndShow(showId, seatNumber);
            return ResponseEntity.ok(seat);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // New endpoint to find multiple seats by seat numbers
//    @PostMapping("/{showId}/seats/find-multiple")
//    public ResponseEntity<List<Seats>> findMultipleSeatsBySeatNumbers(
//            @PathVariable Long showId,
//            @RequestBody List<String> seatNumbers) {
//        try {
//            List<Seats> seats = showService.findMultipleSeatsBySeatNumbersAndShow(showId, seatNumbers);
//            return ResponseEntity.ok(seats);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
//        }
//    }
}