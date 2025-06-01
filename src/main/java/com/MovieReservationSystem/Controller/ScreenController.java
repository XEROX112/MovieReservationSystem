package com.MovieReservationSystem.Controller;

import com.MovieReservationSystem.DTO.AddScreen;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Service.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/screen")
@PreAuthorize("hasRole('ADMIN')")
public class ScreenController {
    private  final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @PostMapping("/{id}/add-screen")
    public ResponseEntity<Screen> addScreenToTheatre(@RequestBody AddScreen addScreen, @PathVariable Long id) {
        Screen screen = screenService.addScreenInTheatre(id, addScreen);
        return (screen != null) ? ResponseEntity.ok(screen) : ResponseEntity.notFound().build();
    }

}
