package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.AddScreen;
import com.MovieReservationSystem.Model.Screen;
import com.MovieReservationSystem.Model.Theatre;
import com.MovieReservationSystem.Repository.ScreenRepository;
import com.MovieReservationSystem.Repository.TheatreRepository;
import com.MovieReservationSystem.Service.ScreenService;
import com.MovieReservationSystem.Service.SeatCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

@Service
public class ScreenServiceImplementation implements ScreenService {
    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final SeatCategoryService seatCategoryService;
    public ScreenServiceImplementation(TheatreRepository theatreRepository, ScreenRepository screenRepository, SeatCategoryService seatCategoryService) {
        this.theatreRepository = theatreRepository;
        this.screenRepository = screenRepository;
        this.seatCategoryService = seatCategoryService;
    }

    @Override
    @Transactional
    public Screen addScreenInTheatre(Long theatreId, AddScreen addScreen) {
        Optional<Theatre> theatre = theatreRepository.findById(theatreId);
        if(theatre.isEmpty()) {
            throw new IllegalArgumentException("There is no such theatre");
        }
        Screen screen = new Screen();
        screen.setScreenName(addScreen.getScreenName());
        screen.setTheater(theatre.get());

        screen = screenRepository.save(screen);


        if (addScreen.getCategoryPrices() != null && addScreen.getSeatCategoryConfiguration() != null) {
            List<Integer> categoryPrices = addScreen.getCategoryPrices();
            List<String> seatCategories = new ArrayList<>(addScreen.getSeatCategoryConfiguration().keySet());
            int previousCategoryRow=0;
            if (categoryPrices.size() == seatCategories.size()) {
                for (int i = 0; i < categoryPrices.size(); i++) {
                    Integer price = categoryPrices.get(i);
                    String category = seatCategories.get(i);

                    Vector<Integer> seats = addScreen.getSeatCategoryConfiguration().get(category);

                    if (price != null && seats != null && category != null) {
                       seatCategoryService.addSeatCategory(screen.getId(),category,price,seats,previousCategoryRow);
                    }
                    previousCategoryRow+=seats.size();
                }
            } else {
                throw new IllegalArgumentException("Mismatch in sizes of categoryPrices and seatCategoryConfiguration");
            }
        }

        theatre.get().getScreens().add(screen);
        theatreRepository.save(theatre.get());
        return screenRepository.save(screen);
    }

    @Override
    public List<Theatre> getAllTheatre() {
        return theatreRepository.findAll();
    }

    @Override
    public Screen getScreenById(Long screenId) {
        Optional<Screen> screen = screenRepository.findById(screenId);
        if(screen.isEmpty()) {
            throw new IllegalArgumentException("There is no such Screen in Theatre");
        }
        return screen.get();
    }

    @Override
    public void deleteScreenById(Long screenId) {
        screenRepository.deleteById(screenId);
    }
}
