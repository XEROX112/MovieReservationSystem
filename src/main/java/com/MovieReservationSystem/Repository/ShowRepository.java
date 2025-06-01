package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByTheater_Id(Long theaterId);

    @Query("SELECT s FROM Show s WHERE s.movie.title = :movieTitle AND s.theater.theaterName = :theatreName")
    List<Show> findShowsByMovieAndTheatre(@Param("movieTitle") String movieTitle,
                                          @Param("theatreName") String theatreName);
}
