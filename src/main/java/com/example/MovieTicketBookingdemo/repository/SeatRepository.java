package com.example.MovieTicketBookingdemo.repository;

import com.example.MovieTicketBookingdemo.model.Seat;
import com.example.MovieTicketBookingdemo.model.SeatId;
import com.example.MovieTicketBookingdemo.model.Showtime;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {
    List<Seat> findByShowtime(Showtime showtime);
    Optional<Seat> findBySeatNumberAndShowtime(String seatNumber, Showtime showtime);
}
