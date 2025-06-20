package com.MovieReservationSystem.Mapper;

import com.MovieReservationSystem.Model.Movie;
import com.MovieReservationSystem.Response.CastDto;
import com.MovieReservationSystem.Response.MovieResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    public static MovieResponseDto mapToDto(Movie movie) {
        List<CastDto> castDtos = movie.getCast().stream()
                .map(p -> new CastDto(p.getName(), p.getRole(), p.getPhoto()))
                .collect(Collectors.toList());

        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .year(movie.getReleaseDate().getYear())
                .genre(movie.getGenre())
                .duration(movie.getDuration() + " min")
                .language(movie.getLanguage())
                .format(movie.getFormat())
                .description(movie.getDescription())
                .about(movie.getAbout())
                .poster(movie.getPoster())
                .cast(castDtos)
                .build();
    }
}
