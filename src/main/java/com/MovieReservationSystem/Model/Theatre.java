package com.MovieReservationSystem.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String theaterName;
    private  String region;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Screen> screens;

}
