package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Show;
import com.MovieReservationSystem.Model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByTheater_Id(Long theaterId);

    @Query("SELECT s FROM Show s JOIN FETCH s.movie JOIN FETCH s.screen WHERE s.theater.id = :theaterId")
    List<Show> findByTheaterIdWithMovie(Long theaterId);

    @Query("SELECT s FROM Show s WHERE s.theater = :theatre AND s.screen = :screen AND TRIM(s.showTime) = TRIM(:time)")
    Optional<Show> findByTheaterAndScreenAndShowTime(@Param("theatre") Theatre theatre,
                                                     @Param("screen") Screen screen,
                                                     @Param("time") String time);

    @Query("SELECT s FROM Show s WHERE s.theater.id IN :theaterIds AND s.movie.id = :movieId")
    List<Show> findByTheaterIdInAndMovieId(@Param("theaterIds") List<Long> theaterIds, @Param("movieId") Long movieId);

}
