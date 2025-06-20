package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Seats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seats, Long> {
    List<Seats> findBySeatRowId(Long rowId);

    Optional<Seats> findBySeatRow_SeatCategory_Screen_AndSeatNumber(Screen screen, String seatNumber);

}
