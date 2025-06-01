package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
    List<Bookings> findByUser(User user);

    void deleteByUserIdAndShowId(Long userId, Long showId);

    Optional<Bookings> findByUserIdAndShowId(Long userId, Long showId);
}

