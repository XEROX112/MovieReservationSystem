package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Model.SeatRow;
import com.MovieReservationSystem.Model.Seats;
import com.MovieReservationSystem.Repository.SeatRepository;
import com.MovieReservationSystem.Repository.SeatRowRepository;
import com.MovieReservationSystem.Service.SeatRowService;
import com.MovieReservationSystem.Service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatServiceImplementation implements SeatService {
    private final SeatRepository seatRepository;
    private final SeatRowRepository seatRowRepository;
    public SeatServiceImplementation(SeatRepository seatRepository, SeatRowRepository seatRowRepository) {
        this.seatRepository = seatRepository;
        this.seatRowRepository = seatRowRepository;
    }

    @Override
    @Transactional
    public void createSeatsInRow(SeatRow seatRow, int columns) {
        List<Seats> seats = new ArrayList<>();

        for (int i = 1; i <= columns; i++) {
            Seats seat = new Seats();
            seat.setSeatNumber(seatRow.getRowName() + i); // Example: A1, A2...
            seat.setSeatRow(seatRow);
            seat.setIsAvailable(true);
            seats.add(seat);
        }

        // ✅ Ensure seatRow.getSeats() is initialized before adding
        if (seatRow.getSeats() == null) {
            seatRow.setSeats(new ArrayList<>());
        }

        seatRow.getSeats().addAll(seats);

        // ✅ Only save seatRow (JPA will automatically save seats)
        seatRowRepository.save(seatRow);
    }


    @Override
    public List<Seats> getSeatsByRow(Long rowId) {
        return seatRepository.findBySeatRowId(rowId);
    }
}
