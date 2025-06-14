package com.MovieReservationSystem.Model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Embeddable
@Data
public class professionalIdentity {
    private String name;
    private String role;
    private String photo;
}
