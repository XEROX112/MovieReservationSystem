package com.MovieReservationSystem.Model;

public enum Format {
    TWO_D("2D"),
    THREE_D("3D"),
    IMAX_TWO_D("IMAX 2D"),
    IMAX_THREE_D("IMAX 3D"),
    FOUR_D_X("4DX");

    private final String displayName;
    Format(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
