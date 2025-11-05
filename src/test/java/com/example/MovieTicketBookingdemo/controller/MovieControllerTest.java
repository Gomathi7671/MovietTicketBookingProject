package com.example.MovieTicketBookingdemo.controller;

import com.example.MovieTicketBookingdemo.Controller.MovieController;
import com.example.MovieTicketBookingdemo.exception.MovieNotFoundException;
import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.model.Showtime;
import com.example.MovieTicketBookingdemo.repository.MovieRepository;
import com.example.MovieTicketBookingdemo.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MovieControllerTest {

    @Mock
    private MovieRepository movieRepo;

    @Mock
    private ShowtimeRepository showtimeRepo;

    @Mock
    private Model model;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    private List<Movie> movies;
    private List<Showtime> showtimes;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

        // ✅ Movies setup
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setGenre("Sci-Fi");
        movie1.setDuration("148");
        movie1.setDescription("Mind-bending thriller");

        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Avatar");
        movie2.setGenre("Action");
        movie2.setDuration("162");
        movie2.setDescription("Epic sci-fi adventure");

        movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);

        // ✅ Showtimes setup
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setShowTime(LocalTime.of(10, 0));
        showtime1.setMovie(movie1);

        showtimes = new ArrayList<>();
        showtimes.add(showtime1);
    }

    @Test
    void testShowMovies_Success() {
        when(movieRepo.findAll()).thenReturn(movies);

        String viewName = movieController.showMovies(model);

        verify(model).addAttribute("movies", movies);
        assertEquals("movies", viewName);
    }

    @Test
    void testShowMovies_NoMovies() {
        when(movieRepo.findAll()).thenReturn(new ArrayList<>());

        String viewName = movieController.showMovies(model);

        verify(model).addAttribute(eq("error"), anyString());
        assertEquals("error", viewName);
    }

    @Test
    void testGetMovieDetails_Success() {
        Movie movie = movies.get(0);
        when(movieRepo.findById(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepo.findByMovieId(1L)).thenReturn(showtimes);

        String viewName = movieController.getMovieDetails(1L, model);

        verify(model).addAttribute("movie", movie);
        verify(model).addAttribute("showtimes", showtimes);
        assertEquals("movie-details", viewName);
    }

    @Test
    void testGetMovieDetails_NotFound() {
        when(movieRepo.findById(1L)).thenReturn(Optional.empty());

        String viewName = movieController.getMovieDetails(1L, model);

        verify(model).addAttribute(eq("error"), anyString());
        assertEquals("error", viewName);
    }
}
