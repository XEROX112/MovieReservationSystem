package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.AddScreen;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Theatre;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ScreenService {
    public Screen addScreenInTheatre(Long theatreId, AddScreen addScreen);
    public List<Theatre> getAllTheatre();
    public Screen getScreenById(Long screenId);
    public void deleteScreenById(Long screenId);
}
