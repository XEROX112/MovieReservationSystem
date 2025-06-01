package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Model.SeatCategory;
import com.MovieReservationSystem.Model.SeatRow;
import com.MovieReservationSystem.Repository.SeatCategoryRepository;
import com.MovieReservationSystem.Repository.SeatRowRepository;
import com.MovieReservationSystem.Service.SeatCategoryService;
import com.MovieReservationSystem.Service.SeatRowService;
import com.MovieReservationSystem.Service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

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
    @Transactional
    public void addSeatRows(Long categoryId, Vector<Integer> rowConfig, int previousCategoryRow) {
        // ✅ Directly check if the category exists via repository (no dependency on SeatCategoryService)
        SeatCategory seatCategory = seatCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Seat Category not found"));

        List<SeatRow> seatRows = new ArrayList<>();

        char rowLetter = (char) ('A' + previousCategoryRow);
        for (int columns : rowConfig) {
            SeatRow seatRow = new SeatRow();
            seatRow.setRowName(String.valueOf(rowLetter));
            seatRow.setColumnCount(columns);
            seatRow.setSeatCategory(seatCategory); // ✅ Store category ID instead of full object
            seatRow = seatRowRepository.save(seatRow);

            // ✅ Create seats inside this row
            seatService.createSeatsInRow(seatRow, columns);

            seatRows.add(seatRow);
            rowLetter++;
        }
        seatCategory.getSeatRows().addAll(seatRows);
        seatCategoryRepository.save(seatCategory);
    }

    @Override
    public List<SeatRow> getRowsByCategory(Long categoryId) {
        return List.of();
    }

    @Override
    @Transactional
    public void updateSeatRows(Long categoryId, Vector<Integer> newRowConfig) {
        List<SeatRow> existingRows = seatRowRepository.findBySeatCategoryId(categoryId);
        seatRowRepository.deleteAll(existingRows);
        SeatCategory seatCategory = seatCategoryRepository.findById(categoryId).get();
        char rowLetter = 'A';
        for (int columns : newRowConfig) {
            SeatRow seatRow = new SeatRow();
            seatRow.setRowName(String.valueOf(rowLetter));
            seatRow.setColumnCount(columns);
            seatRow.setSeatCategory(seatCategory);

            seatRow = seatRowRepository.save(seatRow);
            seatService.createSeatsInRow(seatRow, columns);
            rowLetter++;
        }
    }
}

