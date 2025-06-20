package com.MovieReservationSystem.Repository;

import com.MovieReservationSystem.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findByTitle(String title);

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.theatre t WHERE LOWER(TRIM(t.region)) = LOWER(TRIM(:region))")
    List<Movie> findMoviesByRegion(@Param("region") String region);


    boolean existsByTitle(String title);

    List<Movie> findMoviesByTheatre_Id(Long theaterId);
}
