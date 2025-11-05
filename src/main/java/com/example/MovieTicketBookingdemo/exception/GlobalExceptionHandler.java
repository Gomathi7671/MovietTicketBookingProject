package com.example.MovieTicketBookingdemo.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "register";
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public String handlePasswordMismatch(PasswordMismatchException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "register";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "login";
    }

    // Fallback for any unexpected exceptions
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("error", "Something went wrong: " + ex.getMessage());
        return "error"; // create error.html
    }

    @ExceptionHandler(MovieUploadException.class)
    public String handleMovieUpload(MovieUploadException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "uploadmovies"; // show upload form again
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public String handleMovieNotFound(MovieNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "manage-movies"; // show movie list again
    }

    @ExceptionHandler(ShowtimeCreationException.class)
    public String handleShowtimeCreation(ShowtimeCreationException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "manage-movies";
    }

    // ⚠️ User not logged in
    @ExceptionHandler(UserNotLoggedInException.class)
    public String handleUserNotLoggedIn(UserNotLoggedInException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "booking-confirmation";
    }

    // ⚠️ No seats selected
    @ExceptionHandler(NoSeatsSelectedException.class)
    public String handleNoSeatsSelected(NoSeatsSelectedException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "booking-confirmation";
    }

    // ⚠️ Seat already booked
    @ExceptionHandler(SeatAlreadyBookedException.class)
    public String handleSeatAlreadyBooked(SeatAlreadyBookedException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "booking-confirmation";
    }

    // ⚠️ Booking failure
    @ExceptionHandler(BookingFailureException.class)
    public String handleBookingFailure(BookingFailureException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "booking-confirmation";
    }

    
    }


