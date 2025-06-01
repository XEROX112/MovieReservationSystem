package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.Model.Show;
import com.MovieReservationSystem.Request.AddShowRequest;
import com.MovieReservationSystem.Response.DeleteShowMessage;
import com.MovieReservationSystem.Service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/shows")
public class ShowController {
    private final ShowService showService;
    public ShowController(ShowService showService) {
        this.showService = showService;
    }


    @PostMapping("/add/{id}")
    public ResponseEntity<Show> addShow(
            @PathVariable Long id,
            @RequestBody AddShowRequest addShow) {

        Show show = showService.addShow(id, addShow);

        return (show != null)
                ? ResponseEntity.ok(show) // ✅ 200 OK if show is successfully created
                : ResponseEntity.notFound().build(); // ✅ 404 Not Found if show is null
    }



    @PostMapping("/update_show/{theatreId}")
    public ResponseEntity<Show> updateShow(
            @PathVariable Long theatreId,
            @RequestParam Long showId,
            @RequestBody AddShowRequest addShow) {

        Show show = showService.updateShow(theatreId, showId, addShow);

        if (show != null) {
            return new ResponseEntity<>(show, HttpStatus.CREATED); // ✅ Correct syntax
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // ✅ Handle null case properly
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteShowMessage>deleteShow( @RequestParam Long showId){
        showService.deleteShow(showId);
        DeleteShowMessage message = new DeleteShowMessage();
        message.setMessage("Show deleted successfully");
        return ResponseEntity.ok(message);
    }


    @GetMapping("/{movieTitle}/theatre/{theatreName}")
    public ResponseEntity<List<Show>> getShowsByMovieAndTheatre(@PathVariable String movieTitle,
                                                                @PathVariable String theatreName) {
        List<Show> shows = showService.getShowsByMovieAndTheatre(movieTitle, theatreName);
        return ResponseEntity.ok(shows);
    }
}
