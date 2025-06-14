package com.MovieReservationSystem.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;
    private Boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "seat_row_id", nullable = false)
    @JsonBackReference
    private SeatRow seatRow;
}
