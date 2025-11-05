package com.example.MovieTicketBookingdemo.model;
  import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "showtime")
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String theatreName;
    private String city;
    private LocalDate showDate;
  

@JsonFormat(pattern = "hh:mm a")
private LocalTime showTime;

    private boolean cancellationAvailable;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    // Default constructor
    public Showtime() {
    }

    // All-arguments constructor
    public Showtime(Long id, String theatreName, String city, LocalDate showDate, LocalTime showTime,
                    boolean cancellationAvailable, Movie movie) {
        this.id = id;
        this.theatreName = theatreName;
        this.city = city;
        this.showDate = showDate;
        this.showTime = showTime;
        this.cancellationAvailable = cancellationAvailable;
        this.movie = movie;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    // Optional: useful for debugging/logging
    @Override
    public String toString() {
        return "Showtime{" +
                "id=" + id +
                ", theatreName='" + theatreName + '\'' +
                ", city='" + city + '\'' +
                ", showDate=" + showDate +
                ", showTime=" + showTime +
                ", cancellationAvailable=" + cancellationAvailable +
                ", movie=" + (movie != null ? movie.getTitle() : "null") +
                '}';
    }
}

