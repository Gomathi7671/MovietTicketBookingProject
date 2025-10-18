package com.example.MovieTicketBookingdemo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private Seat seat;  // ✅ this line enables setSeat() via Lombok
}
