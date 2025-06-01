package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Show;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Repository.ScreenRepository;
import com.MovieReservationSystem.Repository.ShowRepository;
import com.MovieReservationSystem.Repository.TheatreRepository;
import com.MovieReservationSystem.Request.AddShowRequest;
import com.MovieReservationSystem.Service.MovieService;
import com.MovieReservationSystem.Service.ShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShowServiceImplementation implements ShowService {

    private final ShowRepository showRepository;
    private  final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final MovieService movieService;
    public ShowServiceImplementation(ShowRepository showRepository, TheatreRepository theatreRepository, ScreenRepository screenRepository, MovieService movieService) {
        this.showRepository = showRepository;
        this.theatreRepository = theatreRepository;
        this.screenRepository = screenRepository;
        this.movieService = movieService;
    }


    @Override
    @Transactional
    public Show addShow(Long theatreId, AddShowRequest addShow) {
        // Find the theatre first
        Optional<Theatre> theatre = theatreRepository.findById(theatreId);
        if (theatre.isEmpty()) {
            throw new RuntimeException("Theatre Not Found. Please add the theatre");
        }

        // Find the screen in the given theatre
        Screen screen = screenRepository.findByScreenNameAndTheater_Id(addShow.getScreenNo(), theatreId);

        // ✅ Corrected: Check if screen is NULL
        if (screen == null) {
            throw new RuntimeException("Screen Not Found in Theatre");
        }

        // Create a new Show
        Show show = new Show();
        show.setShowTime(addShow.getShowtime());
        show.setShowDate(addShow.getShowDate());
        show.setMovie(addShow.getMovie());
        show.setScreen(screen);
        show.setTheater(theatre.get());

        return showRepository.save(show);
    }


    @Override
    public void deleteShow(Long showId) {
             showRepository.deleteById(showId);
    }

    @Override
    public List<Show> getAllShows(Long theatreId) {
        return  showRepository.findByTheater_Id(theatreId);
    }

    @Override
    public Show updateShow(Long theatreId, Long showId, AddShowRequest addShow) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show Not Found. Please add the show"));
        if(addShow.getShowDate()!=null){
            show.setShowDate(addShow.getShowDate());
        }
        if(addShow.getMovie()!=null){
            show.setMovie(addShow.getMovie());
        }
        if(addShow.getScreenNo()!=null){
            Screen  screen =screenRepository.findByScreenNameAndTheater_Id(addShow.getScreenNo(), theatreId);
            if(addShow.getScreenNo()!=null){
                screen.setScreenName(addShow.getScreenNo());
            }
            show.setScreen(screen);
        }
        if(addShow.getShowtime()!=null){
            show.setShowTime(addShow.getShowtime());
        }
        return showRepository.save(show);
    }

    @Override
    public List<Show> getShowsByMovieAndTheatre(String movieTitle, String theatreName) {
        return showRepository.findShowsByMovieAndTheatre(movieTitle, theatreName);
    }
}
