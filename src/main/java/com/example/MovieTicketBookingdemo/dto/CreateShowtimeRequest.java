package com.example.MovieTicketBookingdemo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateShowtimeRequest {

    private Long movieId;
    private String theatreName;
    private String city;
    private LocalDate showDate;
    private LocalTime showTime;
    private boolean cancellationAvailable;

    // Default constructor
    public CreateShowtimeRequest() {
    }

    // All-arguments constructor (optional)
    public CreateShowtimeRequest(Long movieId, String theatreName, String city,
                                 LocalDate showDate, LocalTime showTime,
                                 boolean cancellationAvailable) {
        this.movieId = movieId;
        this.theatreName = theatreName;
        this.city = city;
        this.showDate = showDate;
        this.showTime = showTime;
        this.cancellationAvailable = cancellationAvailable;
    }

    // Getters and Setters
    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalTime showTime) {
        this.showTime = showTime;
    }

    public boolean isCancellationAvailable() {
        return cancellationAvailable;
    }

    public void setCancellationAvailable(boolean cancellationAvailable) {
        this.cancellationAvailable = cancellationAvailable;
    }

    // Optional: for debugging/logging
    @Override
    public String toString() {
        return "CreateShowtimeRequest{" +
                "movieId=" + movieId +
                ", theatreName='" + theatreName + '\'' +
                ", city='" + city + '\'' +
                ", showDate=" + showDate +
                ", showTime=" + showTime +
                ", cancellationAvailable=" + cancellationAvailable +
                '}';
    }
}
