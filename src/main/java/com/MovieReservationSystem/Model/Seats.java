package com.MovieReservationSystem.Model;

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

    private String seatNumber; // Example: "A1", "A2", "B5"
    private Boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "seat_row_id", nullable = false)
    private SeatRow seatRow;
}
