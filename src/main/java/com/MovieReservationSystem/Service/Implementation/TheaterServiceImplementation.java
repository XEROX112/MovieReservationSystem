package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.AddTheatreRequest;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Model.User;
import com.MovieReservationSystem.Repository.TheatreRepository;
import com.MovieReservationSystem.Repository.UserRepository;
import com.MovieReservationSystem.Response.TheaterMovieResponse;
import com.MovieReservationSystem.Service.ScreenService;
import com.MovieReservationSystem.Service.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@PreAuthorize("hasRole('ADMIN')")
public class TheaterServiceImplementation implements TheaterService {
    private final TheatreRepository theatreRepository;
    private final ScreenService screenService;
    private final UserRepository userRepository;

    public TheaterServiceImplementation(TheatreRepository theatreRepository, ScreenService screenService, UserRepository userRepository) {
        this.theatreRepository = theatreRepository;
        this.screenService = screenService;
        this.userRepository = userRepository;
    }

    @Override
    public Theatre addTheater(AddTheatreRequest addTheatre) {
        // ✅ Check if the theater already exists
        if (theatreRepository.findByTheaterNameAndAddressAndRegion(addTheatre.getTheaterName(), addTheatre.getAddress(), addTheatre.getRegion()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Theater already exists");
        }

        // ✅ Map DTO to Entity
        User user = userRepository.findById(addTheatre.getUserId()).get();
        Theatre newTheatre = new Theatre();
        newTheatre.setTheaterName(addTheatre.getTheaterName());
        newTheatre.setRegion(addTheatre.getRegion());
        newTheatre.setAddress(addTheatre.getAddress());
        newTheatre.setUser(user);
        theatreRepository.save(newTheatre);
        long id = newTheatre.getId();
        screenService.addScreenInTheatre(id, addTheatre.getScreens());
        return newTheatre;
    }


    @Override
    public List<Theatre> getAllTheaters() {
        return theatreRepository.findAll();
    }

    @Override
    public List<Theatre> getTheaterByUserId(Long userId) {
        List<Theatre> theatres = theatreRepository.findByUser_Id(userId);
        if (theatres.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No theatres found for this user");
        }
        return theatres;
    }

    @Override
    public boolean deleteTheatre(Long id) {
        if (theatreRepository.existsById(id)) {
            theatreRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Theatre updateTheatre(Long id, AddTheatreRequest updatedTheatre) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Theatre not found"));

        theatre.setTheaterName(updatedTheatre.getTheaterName());

        if (updatedTheatre.getRegion() == null || updatedTheatre.getRegion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Region cannot be empty");
        }

        theatre.setRegion(updatedTheatre.getRegion());
        theatre.setAddress(updatedTheatre.getAddress());
        screenService.updateScreens(id, updatedTheatre.getScreens());
        return theatreRepository.save(theatre);
    }


    @Override
    public long countTheatres() {
        return theatreRepository.count();
    }

    @Override
    public List<Theatre> searchTheatreByName(String name) {
        return theatreRepository.findByTheaterNameContainingIgnoreCase(name);
    }

    @Override
    public boolean doesTheatreExist(String name) {
        return theatreRepository.existsByTheaterName(name);
    }

    @Override
    public List<Theatre> getTheatresByMovieAndRegion(String movieTitle, String region) {
        return theatreRepository.findTheatresByMovieAndRegion(movieTitle, region);
    }

    @Override
    public List<TheaterMovieResponse> getResponseByUser(Long userId) {
        List<Theatre> theatres = theatreRepository.findByUser_Id(userId);
        if (theatres.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No theatres found for this user");
        }
        List<TheaterMovieResponse> responses = new ArrayList<>();
        for (Theatre theatre : theatres) {
            TheaterMovieResponse response = new TheaterMovieResponse();
            response.setTheaterId(theatre.getId());
            response.setTheaterName(theatre.getTheaterName());
            response.setTheaterLocation(theatre.getAddress());
            response.setTheaterRegion(theatre.getRegion());
            responses.add(response);
        }
        return responses;
    }
}
