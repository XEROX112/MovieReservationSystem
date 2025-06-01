package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.SeatRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRowRepository extends JpaRepository<SeatRow,Long> {
    List<SeatRow> findBySeatCategoryId(Long categoryId);

    boolean existsBySeatCategoryId(Long categoryId);
}
