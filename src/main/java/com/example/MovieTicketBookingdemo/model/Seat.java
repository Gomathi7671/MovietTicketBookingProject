package com.example.MovieTicketBookingdemo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
@IdClass(SeatId.class)
public class Seat {

    @Id
    private String seatNumber;

    @Id
    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    private boolean booked;

    // Getters and Setters
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
}
