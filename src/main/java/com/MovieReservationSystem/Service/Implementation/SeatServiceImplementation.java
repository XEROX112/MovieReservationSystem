package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.SeatRequest;
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

    private final SeatRowRepository seatRowRepository;
    private final SeatRepository seatRepository;

    public SeatServiceImplementation(SeatRowRepository seatRowRepository, SeatRepository seatRepository) {
        this.seatRowRepository = seatRowRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    @Transactional
    public void createSeatsInRow(SeatRequest req, SeatRow seatRow) {
        Seats seat = new Seats();
        seat.setSeatNumber(req.getSeatNumber());
        seat.setSeatRow(seatRow);
        seat.setIsAvailable(true);
        seatRepository.save(seat);
    }


    @Override
    public List<Seats> getSeatsByRow(Long rowId) {
        return seatRepository.findBySeatRowId(rowId);
    }
}
