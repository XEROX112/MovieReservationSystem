package com.MovieReservationSystem.Service.Implementation;

import com.MovieReservationSystem.DTO.*;
import com.MovieReservationSystem.Model.*;
import com.MovieReservationSystem.Repository.MovieRepository;
import com.MovieReservationSystem.Repository.ScreenRepository;
import com.MovieReservationSystem.Repository.ShowRepository;
import com.MovieReservationSystem.Repository.TheatreRepository;
import com.MovieReservationSystem.Request.AddShowRequest;
import com.MovieReservationSystem.Response.SeatDto;
import com.MovieReservationSystem.Response.ShowtimeDTO;
import com.MovieReservationSystem.Response.TheaterShowResponseDTO;
import com.MovieReservationSystem.Service.ShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShowServiceImplementation implements ShowService {

    private final ShowRepository showRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;

    public ShowServiceImplementation(ShowRepository showRepository, TheatreRepository theatreRepository, ScreenRepository screenRepository, MovieRepository movieRepository) {
        this.showRepository = showRepository;
        this.theatreRepository = theatreRepository;
        this.screenRepository = screenRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public Show addShow(Long theatreId, AddShowRequest addShow) {
        // Find the theatre first
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre Not Found with ID: " + theatreId));

        // Find the movie by ID
        Movie movie = movieRepository.findById(addShow.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie Not Found with ID: " + addShow.getMovieId()));

        // Find the screen in the given theatre with better error handling
        Screen screen = screenRepository.findByScreenNameAndTheater_Id(addShow.getScreenNo(), theatreId);
        if (screen == null) {
            throw new RuntimeException("Screen '" + addShow.getScreenNo() + "' not found in Theatre ID: " + theatreId);
        }

        // Validate show date and time if needed
        if (addShow.getShowDate() == null || addShow.getShowtime() == null) {
            throw new RuntimeException("Show date and time are required");
        }

        LocalTime time = LocalTime.parse(addShow.getShowtime());
        LocalDate date = LocalDate.parse(addShow.getShowDate());

        Show show = Show.builder()
                .showTime(String.valueOf(time))
                .showDate(date)
                .movie(movie)  // Use the fetched movie object
                .screen(screen)
                .theater(theatre)
                .isAvailable(true)
                .language(addShow.getLanguage())
                .format(addShow.getFormat())
                .build();

        return showRepository.save(show);
    }

    @Override
    public void deleteShow(Long showId) {
        showRepository.deleteById(showId);
    }

    @Override
    public List<Show> getAllShows(Long theatreId) {
        return showRepository.findByTheater_Id(theatreId);
    }

    @Override
    @Transactional
    public Show updateShow(Long movieId, ShowUpdateDTO dto, Long theaterId) {
        try {
            if (dto.getId() == null) {
                throw new RuntimeException("Show ID is required for update.");
            }

            System.out.println("Fetching Show by ID: " + dto.getId());
            Show show = showRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Show not found with ID: " + dto.getId()));

            System.out.println("Validating movie ID...");
            if (!show.getMovie().getId().equals(movieId)) {
                throw new RuntimeException("Show does not belong to Movie ID " + movieId);
            }

            System.out.println("Validating theater ID...");
            if (!show.getTheater().getId().equals(theaterId)) {
                throw new RuntimeException("Show does not belong to Theater ID " + theaterId);
            }

            if (dto.getScreen() != null) {
                System.out.println("Looking for Screen: " + dto.getScreen());
                Screen screen = screenRepository.findByScreenNameAndTheater_Id(dto.getScreen(), theaterId);
                if (screen == null) {
                    throw new RuntimeException("Screen not found: " + dto.getScreen());
                }
                show.setScreen(screen);
            }

            if (dto.getTime() != null) {
                show.setShowTime(dto.getTime().trim());
            }

            if (dto.getLanguage() != null) {
                show.setLanguage(dto.getLanguage());
            }

            if (dto.getFormat() != null) {
                show.setFormat(dto.getFormat());
            }

            System.out.println("Saving updated show...");
            return showRepository.save(show);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in updateShow: " + e.getMessage());
        }
    }

    @Override
    public List<MovieShowsDTO> getShowsByTheater(Long theaterId) {
        List<Show> shows = showRepository.findByTheaterIdWithMovie(theaterId);

        Map<Long, MovieShowsDTO> movieMap = new LinkedHashMap<>();

        for (Show show : shows) {
            Movie movie = show.getMovie();
            String screenName = show.getScreen().getScreenName(); // assume e.g., "Screen-1"

            movieMap.computeIfAbsent(movie.getId(), id -> {
                MovieShowsDTO dto = new MovieShowsDTO();
                dto.setId(movie.getId());
                dto.setTitle(movie.getTitle());
                dto.setPoster(movie.getPoster());
                return dto;
            });

            ShowDTO showDTO = new ShowDTO();
            showDTO.setId(show.getId());
            showDTO.setTime(show.getShowTime());
            showDTO.setLanguage(show.getLanguage());
            showDTO.setFormat(show.getFormat());
            showDTO.setScreen(screenName.replace("Screen-", "")); // Just the number

            movieMap.get(movie.getId()).getShows().add(showDTO);
        }

        return new ArrayList<>(movieMap.values());
    }

    @Override
    public FormatSelectionDto getFormatAndLanguage(String region, Long movieId) {
        System.out.println(region);
        List<Theatre> theaters = theatreRepository.findByRegion(region); // Fetch all theaters by region
        List<Long> theaterIds = theaters.stream().map(Theatre::getId).toList();

        // Fetch all shows for those theaters and the given movie
        System.out.println("Theater IDs: " + theaterIds);
        List<Show> shows = showRepository.findByTheaterIdInAndMovieId(theaterIds, movieId);

        Map<String, Set<String>> map = new HashMap<>();

        for (Show show : shows) {
            String language = show.getLanguage();  // assuming Show has getLanguage()
            String format = show.getFormat();      // assuming Show has getFormat()

            map.computeIfAbsent(language, k -> new HashSet<>()).add(format);
        }

        // Convert Set to List for the DTO
        Map<String, List<String>> resultMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            resultMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        String title = movieRepository.findById(movieId).get().getTitle();
        return new FormatSelectionDto(title, resultMap);
    }

    @Override
    public List<TheaterShowResponseDTO> getShowsByMovieIdAndLangAndFormat(
            Long movieId, String lang, String format, String region, String date) {

        List<Theatre> theatersInRegion = theatreRepository.findByRegion(region);

        List<Theatre> theatersWithMovie = theatersInRegion.stream()
                .filter(theater -> theater.getScreens().stream()
                        .flatMap(screen -> screen.getShows().stream())
                        .anyMatch(show -> show.getMovie().getId().equals(movieId)))
                .collect(Collectors.toList());

        List<TheaterShowResponseDTO> response = new ArrayList<>();

        for (Theatre theater : theatersWithMovie) {
            List<ShowtimeDTO> showtimeDTOs = new ArrayList<>();
            Set<String> formatSet = new HashSet<>(); // to avoid duplicates

            for (Screen screen : theater.getScreens()) {
                for (Show show : screen.getShows()) {
                    boolean matches = show.getMovie().getId().equals(movieId)
                            && show.getLanguage().equalsIgnoreCase(lang)
                            && show.getFormat().equalsIgnoreCase(format)
                            && show.getShowDate().toString().equals(date);

                    if (matches) {
                        ShowtimeDTO dto = new ShowtimeDTO();
                        dto.setId(show.getId());
                        dto.setTime(show.getShowTime().toString());
                        dto.setDate(show.getShowDate().toString());

                        // ✅ Extract prices from seat categories
                        List<Integer> prices = show.getScreen().getSeatCategories().stream()
                                .map(SeatCategory::getPrice)
                                .collect(Collectors.toList());
                        dto.setPrice(prices);

                        showtimeDTOs.add(dto);

                        // ✅ Collect format (from the matched show)
                        formatSet.add(show.getFormat());
                    }
                }
            }

            if (!showtimeDTOs.isEmpty()) {
                TheaterShowResponseDTO dto = new TheaterShowResponseDTO();
                dto.setId(theater.getId());
                dto.setName(theater.getTheaterName());
                dto.setLocation(theater.getAddress());
                dto.setFormats(new ArrayList<>(formatSet)); // convert Set to List
                dto.setShowtimes(showtimeDTOs);
                response.add(dto);
            }
        }

        return response;
    }

    public ShowDetailsDTO getShowDetailsForFrontend(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + showId));

        Screen screen = show.getScreen();
        Theatre theater = screen.getTheater();
        Movie movie = show.getMovie();

        // Transform seat categories with rows and seats
        List<SeatCategoryDTO> seatCategoryDTOs = screen.getSeatCategories().stream()
                .map(this::transformSeatCategory)
                .collect(Collectors.toList());

        return new ShowDetailsDTO(
                show.getId(),
                show.getShowTime(), // Use showTime directly since it's already a formatted string
                show.getShowDate().toString(), // Convert LocalDate to String
                movie.getTitle(),
                movie.getPoster(),
                screen.getId(),
                screen.getScreenName(),
                theater.getTheaterName(),
                seatCategoryDTOs
        );
    }

    @Override
    public List<String> getBookedSeatsForShow(Long showId) {
        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            return List.of();
        }

        // Get all bookings for this show and extract booked seat numbers
        return show.getBookings().stream()
                .flatMap(booking -> booking.getSeats().stream())
                .map(Seats::getSeatNumber)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public SeatDto findSeatBySeatNumberAndShow(Long showId, String seatNumber) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + showId));

        Screen screen = show.getScreen();
        SeatDto dto = new SeatDto();
        for (SeatCategory category : screen.getSeatCategories()) {
            for (SeatRow row : category.getSeatRows()) {
                for (Seats seat : row.getSeats()) {
                    if (seat.getSeatNumber().equals(seatNumber)) {
                        dto.setId(seat.getId());
                        dto.setSeatNumber(seat.getSeatNumber());
                        dto.setSeatCategory(category.getCategoryName());
                        dto.setPrice(category.getPrice());
                        dto.setRowNo(row.getRowName());
                        return dto;
                    }
                }
            }
        }

        throw new RuntimeException("Seat not found: " + seatNumber + " for show: " + showId);
    }

//    @Override
//    public List<Seats> findMultipleSeatsBySeatNumbersAndShow(Long showId, List<String> seatNumbers) {
//        List<Seats> foundSeats = new ArrayList<>();
//
//        for (String seatNumber : seatNumbers) {
//            try {
//                SeatDto seat = findSeatBySeatNumberAndShow(showId, seatNumber);
//                foundSeats.add(seatDto);
//            } catch (RuntimeException e) {
//                throw new RuntimeException("One or more seats not found: " + seatNumber);
//            }
//        }
//
//        return foundSeats;
//    }

    // Updated transformSeatCategory method in ShowService to use actual seat availability
    private SeatCategoryDTO transformSeatCategory(SeatCategory category) {
        // Get all row names for this category
        List<String> rows = category.getSeatRows().stream()
                .map(SeatRow::getRowName)
                .collect(Collectors.toList());

        // Calculate total seats and available seats
        int totalSeats = category.getSeatRows().stream()
                .mapToInt(row -> row.getSeats().size())
                .sum();

        int availableSeats = category.getSeatRows().stream()
                .mapToInt(row -> (int) row.getSeats().stream()
                        .mapToLong(seat -> seat.getIsAvailable() ? 1 : 0)
                        .sum())
                .sum();

        // Get seats per row (assuming consistent within category)
        int seatsPerRow = category.getSeatRows().isEmpty() ? 0 :
                category.getSeatRows().get(0).getColumnCount();

        return new SeatCategoryDTO(
                category.getId(),
                category.getCategoryName(),
                category.getPrice(),
                totalSeats,
                availableSeats,
                rows,
                seatsPerRow
        );
    }

    private String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}