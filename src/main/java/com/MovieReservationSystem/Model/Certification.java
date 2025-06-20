package com.MovieReservationSystem.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Certification {
    U("U"),
    U_A("U/A"),
    A("A"),
    S("S");

    private final String displayName;

    Certification(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Certification fromValue(String value) {
        for (Certification c : Certification.values()) {
            if (c.displayName.equalsIgnoreCase(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown certification: " + value);
    }
}
