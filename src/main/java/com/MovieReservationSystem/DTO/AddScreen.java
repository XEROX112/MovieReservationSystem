package com.MovieReservationSystem.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddScreen {
    @NotBlank
    private String screenName;
    @NotEmpty
    private List<Integer>categoryPrices;
    @NotEmpty
    private LinkedHashMap<String, Vector<Integer>> seatCategoryConfiguration;

}
