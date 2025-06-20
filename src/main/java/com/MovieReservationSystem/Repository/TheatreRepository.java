package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    boolean existsByTheaterName(String theatreName);

    List<Theatre> findByTheaterNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT t FROM Theatre t " +
            "JOIN Show s ON t.id = s.theater.id " +
            "WHERE s.movie.title = :movieTitle AND t.region = :region")
    List<Theatre> findTheatresByMovieAndRegion(@Param("movieTitle") String movieTitle,
                                               @Param("region") String region);

    Optional<Theatre> findByTheaterNameAndAddressAndRegion(String theaterName, String address, String region);

    @Query("SELECT t FROM Theatre t WHERE TRIM(LOWER(t.region)) = TRIM(LOWER(:region))")
    List<Theatre> findByRegion(@Param("region") String region);


    List<Theatre> findByUser(User user);

    List<Theatre> findByUser_Id(Long userId);
}
