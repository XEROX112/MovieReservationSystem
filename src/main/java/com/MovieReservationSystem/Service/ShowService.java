package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.Show;
import com.MovieReservationSystem.Request.AddShowRequest;

import java.util.List;

public interface ShowService {
    public Show addShow(Long theatreId, AddShowRequest addShow);
    public void deleteShow( Long showId);
    public List<Show> getAllShows(Long theatreId);
    public Show updateShow(Long theatreId, Long showId, AddShowRequest addShow);
    public List<Show> getShowsByMovieAndTheatre(String movieTitle, String theatreName);
}
