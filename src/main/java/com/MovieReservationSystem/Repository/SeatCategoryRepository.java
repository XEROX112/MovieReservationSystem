package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.SeatCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatCategoryRepository extends JpaRepository<SeatCategory,Long> {
    List<SeatCategory> findByScreenId(Long screenId);
}
