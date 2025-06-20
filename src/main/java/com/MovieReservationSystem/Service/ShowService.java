package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.FormatSelectionDto;
import com.MovieReservationSystem.DTO.MovieShowsDTO;
import com.MovieReservationSystem.DTO.ShowDetailsDTO;
import com.MovieReservationSystem.DTO.ShowUpdateDTO;
import com.MovieReservationSystem.Model.Seats;
import com.MovieReservationSystem.Model.Show;
import com.MovieReservationSystem.Request.AddShowRequest;
import com.MovieReservationSystem.Response.SeatDto;
import com.MovieReservationSystem.Response.TheaterShowResponseDTO;

import java.util.List;

public interface ShowService {
    public Show addShow(Long theatreId, AddShowRequest addShow);

    public void deleteShow(Long showId);

    public List<Show> getAllShows(Long theatreId);

    public Show updateShow(Long movieId, ShowUpdateDTO dto, Long theatreId);

    public List<MovieShowsDTO> getShowsByTheater(Long theaterId);

    FormatSelectionDto getFormatAndLanguage(String region, Long id);

    List<TheaterShowResponseDTO> getShowsByMovieIdAndLangAndFormat(Long movieId, String lang, String format, String region, String showDate);


    ShowDetailsDTO getShowDetailsForFrontend(Long id);

    List<String> getBookedSeatsForShow(Long showId);


    public SeatDto findSeatBySeatNumberAndShow(Long showId, String seatNumber);
}
