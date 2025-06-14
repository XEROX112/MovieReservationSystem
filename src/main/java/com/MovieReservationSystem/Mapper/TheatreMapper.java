package com.MovieReservationSystem.Mapper;

import com.MovieReservationSystem.Model.*;
import com.MovieReservationSystem.Response.TheatreResponse;

import java.util.ArrayList;
import java.util.List;

public class TheatreMapper {

    public static TheatreResponse toDto(Theatre theatre) {
        TheatreResponse dto = new TheatreResponse();
        dto.setId(theatre.getId());
        dto.setTheaterName(theatre.getTheaterName());
        dto.setAddress(theatre.getAddress());
        dto.setRegion(theatre.getRegion());

        List<TheatreResponse.ScreenResponse> screens = new ArrayList<>();
        for (Screen screen : theatre.getScreens()) {
            TheatreResponse.ScreenResponse screenDto = new TheatreResponse.ScreenResponse();
            screenDto.setId(screen.getId());
            screenDto.setScreenName(screen.getScreenName());

            List<TheatreResponse.SeatCategoryResponse> categories = new ArrayList<>();
            for (SeatCategory cat : screen.getSeatCategories()) {
                TheatreResponse.SeatCategoryResponse catDto = new TheatreResponse.SeatCategoryResponse();
                catDto.setId(cat.getId());
                catDto.setCategoryName(cat.getCategoryName());
                catDto.setPrice(cat.getPrice());

                List<TheatreResponse.SeatRowResponse> rows = new ArrayList<>();
                for (SeatRow row : cat.getSeatRows()) {
                    TheatreResponse.SeatRowResponse rowDto = new TheatreResponse.SeatRowResponse();
                    rowDto.setId(row.getId());
                    rowDto.setRowName(row.getRowName());
                    rowDto.setColumnCount(row.getColumnCount());

                    List<TheatreResponse.SeatResponse> seatDtos = new ArrayList<>();
                    for (Seats seat : row.getSeats()) {
                        TheatreResponse.SeatResponse seatDto = new TheatreResponse.SeatResponse();
                        seatDto.setId(seat.getId());
                        seatDto.setSeatNumber(seat.getSeatNumber());
                        seatDto.setIsAvailable(seat.getIsAvailable());
                        seatDtos.add(seatDto);
                    }

                    rowDto.setSeats(seatDtos);
                    rows.add(rowDto);
                }

                catDto.setSeatRows(rows);
                categories.add(catDto);
            }

            screenDto.setSeatCategories(categories);
            screens.add(screenDto);
        }

        dto.setScreens(screens);
        return dto;
    }
}

