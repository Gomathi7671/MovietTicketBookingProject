package com.example.MovieTicketBookingdemo.exception;

public class BookingFailureException extends RuntimeException {
    public BookingFailureException(String message) {
        super(message);
    }
}
