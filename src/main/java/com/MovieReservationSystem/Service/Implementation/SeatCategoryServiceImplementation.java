package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.SeatCategory;
import com.MovieReservationSystem.Repository.ScreenRepository;
import com.MovieReservationSystem.Repository.SeatCategoryRepository;

import com.MovieReservationSystem.Service.SeatCategoryService;
import com.MovieReservationSystem.Service.SeatRowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public SeatCategory addSeatCategory(Long screenId, String categoryName, int price, Vector<Integer> rowConfig, int previousCategoryRow) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new IllegalArgumentException("Screen not found"));


        SeatCategory seatCategory = new SeatCategory();
        seatCategory.setCategoryName(categoryName);
        seatCategory.setPrice(price);
        seatCategory.setScreen(screen);
        seatCategory = seatCategoryRepository.save(seatCategory);

        // ✅ Instead of passing seatCategory object, just pass categoryId
        seatRowService.addSeatRows(seatCategory.getId(), rowConfig, previousCategoryRow);

        return seatCategory;
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
    public SeatCategory updateSeatCategory(Long categoryId, String newName, Integer newPrice, Vector<Integer> newRowConfig) {
        SeatCategory seatCategory = seatCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Seat Category not found"));

        if (newName != null && !newName.isEmpty()) {
            seatCategory.setCategoryName(newName);
        }
        if (newPrice != null && newPrice > 0) {
            seatCategory.setPrice(newPrice);
        }
        if (newRowConfig != null && !newRowConfig.isEmpty()) {
            // ✅ Just pass categoryId, no direct dependency
            seatRowService.updateSeatRows(categoryId, newRowConfig);
        }

        return seatCategoryRepository.save(seatCategory);
    }
}
