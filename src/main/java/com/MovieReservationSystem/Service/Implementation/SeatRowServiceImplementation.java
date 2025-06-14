package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.RowRequest;
import com.MovieReservationSystem.DTO.SeatRequest;
import com.MovieReservationSystem.Model.SeatCategory;
import com.MovieReservationSystem.Model.SeatRow;
import com.MovieReservationSystem.Repository.SeatCategoryRepository;
import com.MovieReservationSystem.Repository.SeatRowRepository;
import com.MovieReservationSystem.Service.SeatRowService;
import com.MovieReservationSystem.Service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatRowServiceImplementation implements SeatRowService {
    private final SeatRowRepository seatRowRepository;
    private final SeatService seatService;
    private final SeatCategoryRepository seatCategoryRepository;

    public SeatRowServiceImplementation(SeatRowRepository seatRowRepository, SeatService seatService, SeatCategoryRepository seatCategoryRepository) {
        this.seatRowRepository = seatRowRepository;
        this.seatService = seatService;
        this.seatCategoryRepository = seatCategoryRepository;
    }


    @Override
    public List<SeatRow> getRowsByCategory(Long categoryId) {
        return List.of();
    }

    @Transactional
    @Override
    public void updateSeatRows(Long categoryId, RowRequest req, int rowId) {
        List<SeatRow> existingRows = seatRowRepository.findBySeatCategoryId(categoryId);
        seatRowRepository.deleteAll(existingRows);
        SeatCategory seatCategory = seatCategoryRepository.findById(categoryId).get();
        for (SeatRequest seatReq : req.getSeats()) {
            seatService.createSeatsInRow(seatReq, seatCategory.getSeatRows().get(rowId));
        }
    }
}

