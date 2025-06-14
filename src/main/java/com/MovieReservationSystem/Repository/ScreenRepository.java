package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Screen findByScreenNameAndTheater_Id(String screenName, Long theaterId);

    List<Screen> findByTheaterId(Long theaterId);
}
