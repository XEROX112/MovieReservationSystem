package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.SeatCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

public interface SeatCategoryService {
    public SeatCategory addSeatCategory(Long screenId, String categoryName, int price, Vector<Integer> rowConfig,int previousCategoryRow);
    public List<SeatCategory> getSeatCategoriesByScreen(Long screenId);
    public  void deleteSeatCategory(Long screenId);
    public SeatCategory updateSeatCategory(Long categoryId, String newName, Integer newPrice, Vector<Integer> newRowConfig);
}
