package com.example.MovieTicketBookingdemo.service;
import com.example.MovieTicketBookingdemo.service.BookingService;

import com.example.MovieTicketBookingdemo.model.*;
import com.example.MovieTicketBookingdemo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BookingRepository bookingRepository;

    private Movie movie;
    private Showtime showtime;
    private Seat seat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Interstellar");

        showtime = new Showtime();
        showtime.setId(10L);

        seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setShowtime(showtime);
        seat.setBooked(false);
    }

    // ✅ Test: getMovie() success
    @Test
    void testGetMovie_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = bookingService.getMovie(1L);

        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
        verify(movieRepository).findById(1L);
    }

    // ❌ Test: getMovie() throws exception when not found
    @Test
    void testGetMovie_NotFound_ThrowsException() {
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.getMovie(2L));
    }

    // ✅ Test: getShowtime() success
    @Test
    void testGetShowtime_Success() {
        when(showtimeRepository.findById(10L)).thenReturn(Optional.of(showtime));

        Showtime result = bookingService.getShowtime(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(showtimeRepository).findById(10L);
    }

    // ❌ Test: getShowtime() throws exception when not found
    @Test
    void testGetShowtime_NotFound_ThrowsException() {
        when(showtimeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.getShowtime(99L));
    }

    // ✅ Test: getSeatsForShowtime()
    @Test
    void testGetSeatsForShowtime() {
        List<Seat> seats = List.of(seat);
        when(seatRepository.findByShowtime(showtime)).thenReturn(seats);

        List<Seat> result = bookingService.getSeatsForShowtime(showtime);

        assertEquals(1, result.size());
        verify(seatRepository).findByShowtime(showtime);
    }

    // ✅ Test: calculateAmount()
    @Test
    void testCalculateAmount() {
        double total = bookingService.calculateAmount(3, 120.0);
        assertEquals(360.0, total);
    }

    // ✅ Test: confirmBooking success for multiple seats
    @Test
    void testConfirmBooking_Success() {
        List<String> seatNumbers = List.of("A1", "A2");

        Seat seat1 = new Seat();
        seat1.setSeatNumber("A1");
        seat1.setShowtime(showtime);
        seat1.setBooked(false);

        Seat seat2 = new Seat();
        seat2.setSeatNumber("A2");
        seat2.setShowtime(showtime);
        seat2.setBooked(false);

        when(seatRepository.findBySeatNumberAndShowtime("A1", showtime)).thenReturn(Optional.of(seat1));
        when(seatRepository.findBySeatNumberAndShowtime("A2", showtime)).thenReturn(Optional.of(seat2));

        bookingService.confirmBooking("test@gmail.com", movie, showtime, seatNumbers);

        verify(seatRepository, times(2)).save(any(Seat.class));
        verify(bookingRepository, times(2)).save(any(Booking.class));
    }

    // ❌ Test: confirmBooking seat not found
    @Test
    void testConfirmBooking_SeatNotFound_ThrowsException() {
        List<String> seats = List.of("Z9");

        when(seatRepository.findBySeatNumberAndShowtime("Z9", showtime)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                bookingService.confirmBooking("mail@gmail.com", movie, showtime, seats)
        );
    }

    // ❌ Test: confirmBooking seat already booked
    @Test
    void testConfirmBooking_SeatAlreadyBooked_ThrowsException() {
        seat.setBooked(true);
        when(seatRepository.findBySeatNumberAndShowtime("A1", showtime)).thenReturn(Optional.of(seat));

        assertThrows(RuntimeException.class, () ->
                bookingService.confirmBooking("mail@gmail.com", movie, showtime, List.of("A1"))
        );
    }

    // ✅ Test: getBookingById()
    @Test
    void testGetBookingById() {
        Booking booking = new Booking();
        booking.setId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookingRepository).findById(1L);
    }

    // ✅ Test: getBookingById() returns null when not found
    @Test
    void testGetBookingById_NotFound() {
        when(bookingRepository.findById(5L)).thenReturn(Optional.empty());

        Booking result = bookingService.getBookingById(5L);

        assertNull(result);
    }
}
