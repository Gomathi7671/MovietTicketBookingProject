package com.example.MovieTicketBookingdemo.controller;

import com.example.MovieTicketBookingdemo.Controller.BookingController;
import com.example.MovieTicketBookingdemo.exception.*;
import com.example.MovieTicketBookingdemo.model.*;
import com.example.MovieTicketBookingdemo.repository.*;
import com.example.MovieTicketBookingdemo.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private Showtime showtime;
    private Movie movie;
    private Seat seat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        showtime = new Showtime();
        showtime.setId(100L);
        showtime.setMovie(movie);
        showtime.setTheatreName("PVR Chennai");
        showtime.setShowTime(LocalTime.of(10, 0));

        seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setShowtime(showtime);
        seat.setBooked(false);
    }

    // ✅ 1️⃣ User not logged in for seat selection
    @Test
    void testSelectSeats_UserNotLoggedIn_ThrowsException() {
        when(session.getAttribute("email")).thenReturn(null);

        assertThrows(UserNotLoggedInException.class, () ->
                bookingController.selectSeats(100L, session, model));
    }

    // ✅ 2️⃣ Seat selection success
    @Test
    void testSelectSeats_Success() {
        when(session.getAttribute("email")).thenReturn("test@example.com");
        when(bookingService.getShowtime(100L)).thenReturn(showtime);
        when(seatRepository.findAll()).thenReturn(List.of(seat));

        String view = bookingController.selectSeats(100L, session, model);

        verify(model).addAttribute(eq("showtime"), any(Showtime.class));
        verify(model).addAttribute(eq("bookedSeats"), anyList());
        assertEquals("seat-selection", view);
    }

    // ✅ 3️⃣ Confirm booking - not logged in
    @Test
    void testConfirmBooking_UserNotLoggedIn_ThrowsException() {
        when(session.getAttribute("email")).thenReturn(null);

        assertThrows(UserNotLoggedInException.class, () ->
                bookingController.confirmBooking(100L, List.of("A1"), session, model));
    }

    // ✅ 4️⃣ Confirm booking - no seats selected
    @Test
    void testConfirmBooking_NoSeatsSelected_ThrowsException() {
        when(session.getAttribute("email")).thenReturn("test@example.com");

        assertThrows(NoSeatsSelectedException.class, () ->
                bookingController.confirmBooking(100L, new ArrayList<>(), session, model));
    }

    // ✅ 5️⃣ Confirm booking - success
    @Test
    void testConfirmBooking_Success() {
        when(session.getAttribute("email")).thenReturn("test@example.com");
        when(bookingService.getShowtime(100L)).thenReturn(showtime);
        when(seatRepository.findBySeatNumberAndShowtime("A1", showtime))
                .thenReturn(Optional.of(seat));

        String view = bookingController.confirmBooking(100L, List.of("A1"), session, model);

        verify(seatRepository).save(any(Seat.class));
        verify(bookingRepository).save(any(Booking.class));
        verify(model).addAttribute(eq("message"), contains("Seats booked successfully"));
        assertEquals("booking-confirmation", view);
    }

    // ✅ 6️⃣ Confirm booking - seat already booked
    @Test
    void testConfirmBooking_SeatAlreadyBooked_ThrowsException() {
        seat.setBooked(true);
        when(session.getAttribute("email")).thenReturn("test@example.com");
        when(bookingService.getShowtime(100L)).thenReturn(showtime);
        when(seatRepository.findBySeatNumberAndShowtime("A1", showtime))
                .thenReturn(Optional.of(seat));

        assertThrows(SeatAlreadyBookedException.class, () ->
                bookingController.confirmBooking(100L, List.of("A1"), session, model));
    }
}
