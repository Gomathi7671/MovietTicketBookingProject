package com.example.MovieTicketBookingdemo.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String theatreName;
    private String city;

    private LocalDate showDate;   // ✅ FIXED: was String
    private LocalTime showTime;   // ✅ FIXED: was String

    private boolean cancellationAvailable;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}

