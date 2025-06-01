package com.MovieReservationSystem.Model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Embeddable
public class professionalIdentity {
    private String name;
    private String profession;
    private String photo;
}
