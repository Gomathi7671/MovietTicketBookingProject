package com.example.MovieTicketBookingdemo.repository;

import com.example.MovieTicketBookingdemo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // No need to implement anything, JpaRepository gives CRUD methods
}
