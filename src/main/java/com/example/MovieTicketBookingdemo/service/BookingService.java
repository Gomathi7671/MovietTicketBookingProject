package com.example.MovieTicketBookingdemo.service;

import com.example.MovieTicketBookingdemo.model.*;
import com.example.MovieTicketBookingdemo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MovieTicketBookingdemo.model.Showtime; 

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public Showtime getShowtime(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
    }

    public List<Seat> getSeatsForShowtime(Showtime showtime) {
        return seatRepository.findByShowtime(showtime);
    }

    public double calculateAmount(int noOfSeats, double seatPrice) {
        return noOfSeats * seatPrice;
    }

    // After successful payment → confirm booking
    public void confirmBooking(String email, Movie movie, Showtime showtime, List<String> seatNumbers) {
        for (String seatNumber : seatNumbers) {
            Seat seat = seatRepository.findBySeatNumberAndShowtime(seatNumber, showtime)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (seat.isBooked()) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " already booked.");
            }

            // Mark seat booked
            seat.setBooked(true);
            seatRepository.save(seat);

            // Save booking
            Booking booking = new Booking();
            booking.setEmail(email);
            booking.setMovie(movie);
            booking.setShowtime(showtime);
            booking.setSeat(seat);
            booking.setNoOfSeats(seatNumbers.size());

            bookingRepository.save(booking);
        }
    }

    // Add this at the bottom of the class, after existing methods
public Booking getBookingById(Long id) {
    return bookingRepository.findById(id).orElse(null);
}

}

