package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.RowRequest;
import com.MovieReservationSystem.DTO.ScreenRequest;
import com.MovieReservationSystem.DTO.SeatCategoryRequest;
import com.MovieReservationSystem.DTO.SeatRequest;
import com.MovieReservationSystem.Model.*;
import com.MovieReservationSystem.Repository.*;
import com.MovieReservationSystem.Service.ScreenService;
import com.MovieReservationSystem.Service.SeatCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScreenServiceImplementation implements ScreenService {
    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final SeatCategoryRepository seatCategoryRepository;
    private final SeatRepository seatRepository;
    private final SeatRowRepository seatRowRepository;

    public ScreenServiceImplementation(TheatreRepository theatreRepository, ScreenRepository screenRepository, SeatCategoryRepository seatCategoryRepository, SeatRepository seatRepository, SeatRowRepository seatRowRepository) {
        this.theatreRepository = theatreRepository;
        this.screenRepository = screenRepository;
        this.seatCategoryRepository = seatCategoryRepository;
        this.seatRepository = seatRepository;
        this.seatRowRepository = seatRowRepository;
    }

    @Override
    @Transactional
    public List<Screen> addScreenInTheatre(Long theatreId, List<ScreenRequest> screenRequests) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new IllegalArgumentException("There is no such theatre"));

        List<Screen> savedScreens = new ArrayList<>();

        for (ScreenRequest screenReq : screenRequests) {
            Screen screen = new Screen();
            screen.setScreenName(screenReq.getScreenName());
            screen.setTheater(theatre);

            List<SeatCategory> categories = new ArrayList<>();
            for (SeatCategoryRequest categoryReq : screenReq.getSeatCategories()) {
                SeatCategory category = new SeatCategory();
                category.setCategoryName(categoryReq.getCategoryName());
                category.setPrice((int) categoryReq.getPrice());
                category.setScreen(screen); // link category to screen

                List<SeatRow> rows = new ArrayList<>();
                for (RowRequest rowReq : categoryReq.getSeatRows()) {
                    SeatRow row = new SeatRow();
                    row.setRowName(rowReq.getRowName());
                    row.setColumnCount(rowReq.getColumnCount());
                    row.setSeatCategory(category); // link row to category

                    List<Seats> seats = new ArrayList<>();
                    for (SeatRequest seatReq : rowReq.getSeats()) {
                        Seats seat = new Seats();
                        seat.setSeatNumber(seatReq.getSeatNumber());
                        seat.setIsAvailable(true);
                        seat.setSeatRow(row); // link seat to row
                        seats.add(seat);
                    }

                    row.setSeats(seats); // set seat list in row
                    rows.add(row);
                }

                category.setSeatRows(rows); // set row list in category
                categories.add(category);
            }

            screen.setSeatCategories(categories); // set categories in screen
            screenRepository.save(screen); // save entire screen -> cascades everything
            savedScreens.add(screen);
        }

        return savedScreens;
    }


    @Override
    public List<Theatre> getAllTheatre() {
        return theatreRepository.findAll();
    }

    @Override
    public Screen getScreenById(Long screenId) {
        Optional<Screen> screen = screenRepository.findById(screenId);
        if (screen.isEmpty()) {
            throw new IllegalArgumentException("There is no such Screen in Theatre");
        }
        return screen.get();
    }

    @Override
    public void deleteScreenById(Long screenId) {
        screenRepository.deleteById(screenId);
    }

    @Override
    @Transactional
    public List<Screen> updateScreens(Long theaterId, List<ScreenRequest> screenRequests) {
        Theatre theater = theatreRepository.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        List<Screen> existingScreens = screenRepository.findByTheaterId(theaterId);
        Map<String, Screen> screenMap = existingScreens.stream()
                .collect(Collectors.toMap(Screen::getScreenName, screen -> screen));

        for (ScreenRequest screenReq : screenRequests) {
            Screen screen = screenMap.get(screenReq.getScreenName());
            if (screen == null) continue;

            Map<String, SeatCategory> categoryMap = screen.getSeatCategories().stream()
                    .collect(Collectors.toMap(SeatCategory::getCategoryName, category -> category));

            for (SeatCategoryRequest catReq : screenReq.getSeatCategories()) {
                SeatCategory category = categoryMap.get(catReq.getCategoryName());
                if (category == null) continue;

                category.setPrice((int) catReq.getPrice());
            }
        }

        screenRepository.saveAll(existingScreens);
        return existingScreens;
    }


}