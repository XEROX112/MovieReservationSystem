package com.MovieReservationSystem.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Embeddable
@Data
public class professionalIdentity {
    private String name;
    private String role;
    @Column(length = 1000)
    private String photo;
}
