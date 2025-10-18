package com.example.MovieTicketBookingdemo.repository;

import com.example.MovieTicketBookingdemo.model.MovieUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieUserRepository extends JpaRepository<MovieUser, Long> {

    // Find user by email
    MovieUser findByEmail(String email);

    // Find user by username
    MovieUser findByUsername(String username);   // <-- Add this line
}
