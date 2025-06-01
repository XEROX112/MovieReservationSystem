package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Seats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository <Seats,Long>{
    List<Seats> findBySeatRowId(Long rowId);

}
