package com.example.MovieTicketBookingdemo.dto;


import com.example.MovieTicketBookingdemo.model.Showtime;

public class CreateShowtimeResponse {

    private Long id;
    private String message;

    // No-argument constructor
    public CreateShowtimeResponse() {
    }

    // All-argument constructor
    public CreateShowtimeResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Factory method to create a response from a Showtime object
    public static CreateShowtimeResponse from(Showtime s) {
        CreateShowtimeResponse response = new CreateShowtimeResponse();
        response.setId(s.getId());
        response.setMessage("Showtime created successfully");
        return response;
    }

    // Optional: for debugging/logging
    @Override
    public String toString() {
        return "CreateShowtimeResponse{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
