package com.MovieReservationSystem.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SeatRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rowName; // Example: "A", "B", "C"
    private int columnCount; // Number of seats in this row

    @ManyToOne
    @JoinColumn(name = "seat_category_id", nullable = false)
    private SeatCategory seatCategory;

    @OneToMany(mappedBy = "seatRow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seats> seats;

}
