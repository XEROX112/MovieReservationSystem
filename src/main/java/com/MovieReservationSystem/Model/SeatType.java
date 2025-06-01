package com.MovieReservationSystem.Model;

public enum SeatType {
    VIP(500.0),
    PREMIUM(300.0),
    REGULAR(150.0),
    DEFAULT(100.0); // Default price

    private final double price;

    SeatType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
