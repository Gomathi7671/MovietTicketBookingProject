package com.example.MovieTicketBookingdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MovieTicketBookingdemo.model.Showtime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovieId(Long id); // ✅ matches MovieController
}
