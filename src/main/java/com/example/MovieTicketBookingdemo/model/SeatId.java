package com.example.MovieTicketBookingdemo.model;

import java.io.Serializable;
import java.util.Objects;

public class SeatId implements Serializable {

    private String seatNumber;
    private Long showtime; // ✅ Must match the name of the field in Seat.java

    public SeatId() {}

    public SeatId(String seatNumber, Long showtime) {
        this.seatNumber = seatNumber;
        this.showtime = showtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatId)) return false;
        SeatId that = (SeatId) o;
        return Objects.equals(seatNumber, that.seatNumber) &&
               Objects.equals(showtime, that.showtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNumber, showtime);
    }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public Long getShowtime() { return showtime; }
    public void setShowtime(Long showtime) { this.showtime = showtime; }
}
