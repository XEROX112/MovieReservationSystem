package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.ScreenRequest;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Theatre;

import java.util.List;

public interface ScreenService {
    public List<Screen> addScreenInTheatre(Long theatreId, List<ScreenRequest> screenRequest);

    public List<Theatre> getAllTheatre();

    public Screen getScreenById(Long screenId);

    public void deleteScreenById(Long screenId);

    public List<Screen> updateScreens(Long theatreId, List<ScreenRequest> screenRequest);
}
