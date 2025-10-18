package com.example.MovieTicketBookingdemo.model;

import java.io.Serializable;
import java.util.Objects;

public class SeatId implements Serializable {

    private String seatNumber;
    private Long showtime;

    public SeatId() {}
    public SeatId(String seatNumber, Long showtime) {
        this.seatNumber = seatNumber;
        this.showtime = showtime;
    }

    // equals and hashCode required for @IdClass
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatId)) return false;
        SeatId seatId = (SeatId) o;
        return seatNumber.equals(seatId.seatNumber) && showtime.equals(seatId.showtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNumber, showtime);
    }

    // Getters and Setters
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public Long getShowtime() { return showtime; }
    public void setShowtime(Long showtime) { this.showtime = showtime; }
}
