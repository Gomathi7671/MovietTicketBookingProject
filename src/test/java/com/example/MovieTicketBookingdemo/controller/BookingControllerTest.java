package com.example.MovieTicketBookingdemo.controller;

import com.example.MovieTicketBookingdemo.Controller.BookingController;

import com.example.MovieTicketBookingdemo.model.*;
import com.example.MovieTicketBookingdemo.repository.*;
import com.example.MovieTicketBookingdemo.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

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
    private Model model;

    @InjectMocks
    private BookingController bookingController;

    private MockHttpSession session;
    private Movie movie;
    private Showtime showtime;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
        session.setAttribute("email", "user@example.com");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Avengers");

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setTheatreName("Cineplex");
        showtime.setShowTime(LocalTime.parse("18:30")); // ✅ LocalTime
    }

    @Test
    void testSelectSeatsUserNotLoggedIn() {
        session.removeAttribute("email");
        String view = bookingController.selectSeats(1L, session, model);
        assertEquals("redirect:/login", view);
    }

    @Test
    void testSelectSeatsUserLoggedIn() {
        when(bookingService.getShowtime(1L)).thenReturn(showtime);
        when(seatRepository.findAll()).thenReturn(Collections.emptyList());

        String view = bookingController.selectSeats(1L, session, model);
        assertEquals("seat-selection", view);

        verify(model).addAttribute("showtime", showtime);
        verify(model).addAttribute(eq("bookedSeats"), anyList());
    }

    @Test
    void testConfirmBookingNoSeats() {
        String view = bookingController.confirmBooking(1L, null, session, model);
        assertEquals("booking-confirmation", view);
        verify(model).addAttribute("message", "⚠️ No seats selected!");
    }

    @Test
    void testConfirmBookingUserNotLoggedIn() {
        session.removeAttribute("email");
        String view = bookingController.confirmBooking(1L, Arrays.asList("A1"), session, model);
        assertEquals("booking-confirmation", view);
        verify(model).addAttribute("message", "❌ Please login first!");
    }

    @Test
    void testConfirmBookingSuccess() {
        List<String> seats = Arrays.asList("A1", "A2");

        when(bookingService.getShowtime(1L)).thenReturn(showtime);
        when(seatRepository.findBySeatNumberAndShowtime(anyString(), any(Showtime.class)))
                .thenReturn(Optional.empty());

        String view = bookingController.confirmBooking(1L, seats, session, model);
        assertEquals("booking-confirmation", view);
        verify(model).addAttribute(eq("message"), contains("✅ Seats booked successfully"));
    }
}
