package com.example.MovieTicketBookingdemo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateShowtimeRequest {
    private Long movieId;
    private String theatreName;
    private String city;
    private LocalDate showDate;   // ✅ FIXED
    private LocalTime showTime;   // ✅ FIXED
    private boolean cancellationAvailable;
}
