package com.example.MovieTicketBookingdemo.dto;

import com.example.MovieTicketBookingdemo.model.Showtime;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CreateShowtimeResponse {
    private Long id;
    private String message;

    public static CreateShowtimeResponse from(Showtime s) {
        return CreateShowtimeResponse.builder()
                .id(s.getId())
                .message("Showtime created successfully")
                .build();
    }
}

