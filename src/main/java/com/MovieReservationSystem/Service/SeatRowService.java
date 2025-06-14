package com.MovieReservationSystem.Service;

import com.MovieReservationSystem.DTO.RowRequest;
import com.MovieReservationSystem.Model.SeatCategory;
import com.MovieReservationSystem.Model.SeatRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Vector;

public interface SeatRowService {


    public List<SeatRow> getRowsByCategory(Long categoryId);

    public void updateSeatRows(Long categoryId, RowRequest request, int rowId);


}
