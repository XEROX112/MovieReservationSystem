package com.MovieReservationSystem.Repository;


import com.MovieReservationSystem.Model.Bookings;
import com.MovieReservationSystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
     User findByEmail(String email);

    Optional<User> findById(Long id);
}
