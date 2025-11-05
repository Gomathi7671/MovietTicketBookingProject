package com.example.MovieTicketBookingdemo.exception;

public class NoSeatsSelectedException extends RuntimeException {
    public NoSeatsSelectedException(String message) {
        super(message);
    }
}
