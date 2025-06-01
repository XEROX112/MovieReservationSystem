package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.Model.SeatRow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

public interface SeatRowService {
    public void addSeatRows(Long categoryId, Vector<Integer> rowConfig,int previousCategoryRow);
    public List<SeatRow> getRowsByCategory(Long categoryId);
    public void updateSeatRows(Long categoryId, Vector<Integer>  newRowConfig);
}
