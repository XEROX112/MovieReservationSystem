package com.MovieReservationSystem.Model;

public enum Certification {
    U("U"),
    U_A("U/A"),
    A("A"),
    S("S");

    private final String displayName;

    Certification(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
