package com.example.MovieTicketBookingdemo.model;

import jakarta.persistence.*;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private int noOfSeats;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Showtime showtime;

    @ManyToOne
    private Seat seat;

    // ---------- Constructors ----------
    public Booking() {}

    public Booking(Long id, String email, int noOfSeats, Movie movie, Showtime showtime, Seat seat) {
        this.id = id;
        this.email = email;
        this.noOfSeats = noOfSeats;
        this.movie = movie;
        this.showtime = showtime;
        this.seat = seat;
    }

    // ---------- Getters ----------
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public Movie getMovie() {
        return movie;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Seat getSeat() {
        return seat;
    }

    // ---------- Setters ----------
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
