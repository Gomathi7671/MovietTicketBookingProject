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
    @JoinColumn(name = "showtime_id", referencedColumnName = "id")
    private Showtime showtime; // ✅ Must match the field name in SeatId.java

    private boolean booked;

    public Seat() {}

    public Seat(String seatNumber, Showtime showtime, boolean booked) {
        this.seatNumber = seatNumber;
        this.showtime = showtime;
        this.booked = booked;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
