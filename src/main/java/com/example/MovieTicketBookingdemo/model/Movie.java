package com.example.MovieTicketBookingdemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "movie") // ensures table name matches PostgreSQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @Column(length = 1000)
    private String description;

    private String duration;   // e.g., "2h 15m"

    @DecimalMin(value = "0.0", message = "Rating must be >= 0")
    @DecimalMax(value = "10.0", message = "Rating must be <= 10")
    private Double rating;

    private String genre;
@Column(name = "movie_cast")
private String cast;


    private LocalDate releaseDate;

    private String posterUrl;  // Cloudinary URL
}
