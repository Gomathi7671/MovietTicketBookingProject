package com.example.MovieTicketBookingdemo.controller;
import com.example.MovieTicketBookingdemo.Controller.AdminController;
import com.example.MovieTicketBookingdemo.dto.CreateShowtimeRequest;
import com.example.MovieTicketBookingdemo.exception.MovieNotFoundException;
import com.example.MovieTicketBookingdemo.exception.MovieUploadException;
import com.example.MovieTicketBookingdemo.exception.ShowtimeCreationException;
import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.model.Showtime;
import com.example.MovieTicketBookingdemo.repository.MovieRepository;
import com.example.MovieTicketBookingdemo.repository.ShowtimeRepository;
import com.example.MovieTicketBookingdemo.service.CloudinaryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private Model model;

    @Mock
    private MultipartFile posterFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test 1: Show Upload Form
    @Test
    void testShowUploadForm() {
        String viewName = adminController.showUploadForm(model);
        assertEquals("uploadmovies", viewName);
        verify(model, times(1)).addAttribute(eq("movie"), any(Movie.class));
    }

    // ✅ Test 2: Add Movie - Success
    @Test
    void testAddMovie_Success() throws Exception {
        Movie movie = new Movie();
        when(posterFile.isEmpty()).thenReturn(false);
        when(cloudinaryService.uploadFile(posterFile)).thenReturn("https://cloudinary.com/fakeposter.jpg");

        String result = adminController.addMovie(movie, posterFile);

        assertEquals("redirect:/admin/uploadMovie", result);
        verify(movieRepository).save(movie);
        verify(cloudinaryService).uploadFile(posterFile);
    }

    // ✅ Test 3: Add Movie - Missing Poster
    @Test
    void testAddMovie_MissingPoster_ThrowsException() {
        Movie movie = new Movie();
        when(posterFile.isEmpty()).thenReturn(true);

        MovieUploadException ex = assertThrows(
                MovieUploadException.class,
                () -> adminController.addMovie(movie, posterFile)
        );
        assertEquals("Poster image is required!", ex.getMessage());
        verifyNoInteractions(movieRepository);
    }

    // ✅ Test 4: Add Movie - Cloudinary Error
    @Test
    void testAddMovie_CloudinaryError_ThrowsException() throws Exception {
        Movie movie = new Movie();
        when(posterFile.isEmpty()).thenReturn(false);
        when(cloudinaryService.uploadFile(posterFile)).thenThrow(new IOException("Upload failed"));

        MovieUploadException ex = assertThrows(
                MovieUploadException.class,
                () -> adminController.addMovie(movie, posterFile)
        );
        assertTrue(ex.getMessage().contains("Error while storing movie"));
    }

    // ✅ Test 5: Manage Movies - Success
    @Test
    void testManageMovies() {
        List<Movie> movies = List.of(new Movie(), new Movie());
        when(movieRepository.findAll()).thenReturn(movies);

        String result = adminController.manageMovies(model);

        assertEquals("manage-movies", result);
        verify(model).addAttribute("movies", movies);
        verify(model).addAttribute(eq("showtimeRequest"), any(CreateShowtimeRequest.class));
    }

    // ✅ Test 6: Schedule Show - Success
    @Test
    void testScheduleShow_Success() {
        CreateShowtimeRequest req = new CreateShowtimeRequest();
        req.setMovieId(1L);
        req.setCity("Chennai");
        req.setTheatreName("PVR");
        req.setShowDate(LocalDate.of(2025, 11, 10)); // ✅ LocalDate instead of String
    req.setShowTime(LocalTime.of(18, 0));        // ✅ LocalTime instead of String

        Movie movie = new Movie();
        movie.setId(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        String result = adminController.scheduleShow(req);

        assertEquals("redirect:/admin/manageMovies", result);
        verify(showtimeRepository).save(any(Showtime.class));
    }

    // ✅ Test 7: Schedule Show - Movie Not Found
    @Test
    void testScheduleShow_MovieNotFound_ThrowsException() {
        CreateShowtimeRequest req = new CreateShowtimeRequest();
        req.setMovieId(99L);

        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> adminController.scheduleShow(req));
        verifyNoInteractions(showtimeRepository);
    }

    // ✅ Test 8: Schedule Show - Other Exception
    @Test
    void testScheduleShow_OtherException_ThrowsShowtimeCreationException() {
        CreateShowtimeRequest req = new CreateShowtimeRequest();
        req.setMovieId(1L);

        when(movieRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        assertThrows(ShowtimeCreationException.class, () -> adminController.scheduleShow(req));
    }
}
