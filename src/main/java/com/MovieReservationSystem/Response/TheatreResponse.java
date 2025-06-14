package com.MovieReservationSystem.Response;

import lombok.Data;

import java.util.List;

@Data
public class TheatreResponse {
    private Long id;
    private String theaterName;
    private String address;
    private String region;
    private List<ScreenResponse> screens;

    @Data
    public static class ScreenResponse {
        private Long id;
        private String screenName;
        private List<SeatCategoryResponse> seatCategories;
    }

    @Data
    public static class SeatCategoryResponse {
        private Long id;
        private String categoryName;
        private int price;
        private List<SeatRowResponse> seatRows;
    }

    @Data
    public static class SeatRowResponse {
        private Long id;
        private String rowName;
        private int columnCount;
        private List<SeatResponse> seats;
    }

    @Data
    public static class SeatResponse {
        private Long id;
        private String seatNumber;
        private Boolean isAvailable;
    }
}

