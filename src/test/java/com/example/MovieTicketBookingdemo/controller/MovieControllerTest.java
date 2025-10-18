package com.example.MovieTicketBookingdemo.controller;

import com.example.MovieTicketBookingdemo.Controller.MovieController;

import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.model.Showtime;
import com.example.MovieTicketBookingdemo.repository.MovieRepository;
import com.example.MovieTicketBookingdemo.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @Mock
    private MovieRepository movieRepo;

    @Mock
    private ShowtimeRepository showtimeRepo;

    @Mock
    private Model model;

    @InjectMocks
    private MovieController movieController;

    private Movie movie;
    private Showtime showtime;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setTheatreName("Cineplex");
        showtime.setShowTime(LocalTime.of(18, 30));
    }

    @Test
    void testShowMovies() {
        List<Movie> movies = Arrays.asList(movie);
        when(movieRepo.findAll()).thenReturn(movies);

        String viewName = movieController.showMovies(model);
        assertEquals("movies", viewName);
        verify(model).addAttribute("movies", movies);
    }

    @Test
    void testGetMovieDetailsMovieExists() {
        when(movieRepo.findById(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepo.findByMovieId(1L)).thenReturn(Arrays.asList(showtime));

        String viewName = movieController.getMovieDetails(1L, model);
        assertEquals("movie-details", viewName);
        verify(model).addAttribute("movie", movie);
        verify(model).addAttribute("showtimes", Arrays.asList(showtime));
    }

    @Test
    void testGetMovieDetailsMovieNotFound() {
        when(movieRepo.findById(2L)).thenReturn(Optional.empty());

        String viewName = movieController.getMovieDetails(2L, model);
        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Movie not found!");
    }
}
