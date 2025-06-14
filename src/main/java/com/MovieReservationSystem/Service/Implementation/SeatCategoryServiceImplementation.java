package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.RowRequest;
import com.MovieReservationSystem.DTO.SeatCategoryRequest;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.SeatCategory;
import com.MovieReservationSystem.Repository.ScreenRepository;
import com.MovieReservationSystem.Repository.SeatCategoryRepository;

import com.MovieReservationSystem.Service.SeatCategoryService;
import com.MovieReservationSystem.Service.SeatRowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

@Service
public class SeatCategoryServiceImplementation implements SeatCategoryService {

    private final SeatCategoryRepository seatCategoryRepository;
    private final SeatRowService seatRowService;
    private final ScreenRepository screenRepository;

    public SeatCategoryServiceImplementation(SeatCategoryRepository seatCategoryRepository,
                                             SeatRowService seatRowService, ScreenRepository screenRepository
    ) {
        this.seatCategoryRepository = seatCategoryRepository;
        this.seatRowService = seatRowService;
        this.screenRepository = screenRepository;
    }


    @Override
    public List<SeatCategory> getSeatCategoriesByScreen(Long screenId) {
        return seatCategoryRepository.findByScreenId(screenId);
    }

    @Override
    public void deleteSeatCategory(Long screenId) {
        seatCategoryRepository.deleteById(screenId);
    }

    @Override
    @Transactional
    public SeatCategory updateSeatCategory(SeatCategoryRequest req, Long categoryId, int seatRowId) {
        SeatCategory seatCategory = seatCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Seat Category not found"));

        if (req.getCategoryName() != null && !req.getCategoryName().isEmpty()) {
            seatCategory.setCategoryName(req.getCategoryName());
        }
        if ((int) req.getPrice() != seatCategory.getPrice()) {
            seatCategory.setPrice((int) req.getPrice());
        }
        for (RowRequest row : req.getSeatRows()) {
            seatRowService.updateSeatRows(categoryId, row, seatRowId);
        }

        return seatCategoryRepository.save(seatCategory);
    }
}
