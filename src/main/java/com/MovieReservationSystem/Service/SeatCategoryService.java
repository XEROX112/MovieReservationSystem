package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.SeatCategoryRequest;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.SeatCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

public interface SeatCategoryService {


    public List<SeatCategory> getSeatCategoriesByScreen(Long screenId);

    public void deleteSeatCategory(Long screenId);

    public SeatCategory updateSeatCategory(SeatCategoryRequest req, Long categoryId, int seatRowId);
}
