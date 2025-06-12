package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.AddTheatre;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Repository.TheatreRepository;
import com.MovieReservationSystem.Service.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
@PreAuthorize("hasRole('ADMIN')")
public class TheaterServiceImplementation implements TheaterService {
    private final TheatreRepository theatreRepository;

    public TheaterServiceImplementation(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @Override
    public Theatre addTheater(Theatre addTheatre) {
        // ✅ Check if the theater already exists
        if (theatreRepository.findByTheaterNameAndAddressAndRegion(addTheatre.getTheaterName(), addTheatre.getAddress(), addTheatre.getRegion()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Theater already exists");
        }

        // ✅ Map DTO to Entity
        Theatre newTheatre = new Theatre();
        newTheatre.setTheaterName(addTheatre.getTheaterName());
        newTheatre.setRegion(addTheatre.getRegion());

        return theatreRepository.save(newTheatre);
    }


    @Override
    public List<Theatre> getAllTheaters() {
        return theatreRepository.findAll();
    }

    @Override
    public Theatre getTheaterById(Long theaterId) {
        return theatreRepository.findById(theaterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Theatre not found"));
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
    public Theatre updateTheatre(Long id, Theatre updatedTheatre) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Theatre not found"));

        Optional<Theatre> existingTheatre = theatreRepository.findByTheaterNameAndAddressAndRegion(updatedTheatre.getTheaterName(), updatedTheatre.getAddress(), updatedTheatre.getRegion());
        if (existingTheatre.isPresent() && !existingTheatre.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Theatre name already exists");
        }

        theatre.setTheaterName(updatedTheatre.getTheaterName());

        // Ensure region is not null or empty
        if (updatedTheatre.getRegion() == null || updatedTheatre.getRegion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Region cannot be empty");
        }

        // Normalize region string to avoid inconsistencies
        theatre.setRegion(updatedTheatre.getRegion().trim().toLowerCase());

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
}
