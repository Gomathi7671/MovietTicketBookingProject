package com.example.MovieTicketBookingdemo.Controller;

import com.example.MovieTicketBookingdemo.model.*;
import com.example.MovieTicketBookingdemo.repository.*;
import com.example.MovieTicketBookingdemo.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JavaMailSender mailSender;

    // ✅ 1️⃣ Show seat selection page
    @GetMapping({"/select", "/book"})
    public String selectSeats(@RequestParam Long showtimeId, HttpSession session, Model model) {
        // Check login
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        Showtime showtime = bookingService.getShowtime(showtimeId);

        List<String> bookedSeats = seatRepository.findAll().stream()
                .filter(s -> s.getShowtime().getId().equals(showtimeId))
                .filter(Seat::isBooked)
                .map(Seat::getSeatNumber)
                .collect(Collectors.toList());

        model.addAttribute("showtime", showtime);
        model.addAttribute("bookedSeats", bookedSeats);
        return "seat-selection";
    }

    // ✅ 2️⃣ Confirm booking
    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam Long showtimeId,
                                 @RequestParam(required = false) List<String> seatIds,
                                 HttpSession session,
                                 Model model) {

        String email = (String) session.getAttribute("email");
        if (email == null) {
            model.addAttribute("message", "❌ Please login first!");
            return "booking-confirmation";
        }

        if (seatIds == null || seatIds.isEmpty()) {
            model.addAttribute("message", "⚠️ No seats selected!");
            return "booking-confirmation";
        }

        Showtime showtime = bookingService.getShowtime(showtimeId);
        Movie movie = showtime.getMovie();

        try {
            // Book each seat
            for (String seatNumber : seatIds) {
                Seat seat = seatRepository.findBySeatNumberAndShowtime(seatNumber, showtime)
                        .orElseGet(() -> {
                            Seat newSeat = new Seat();
                            newSeat.setSeatNumber(seatNumber);
                            newSeat.setShowtime(showtime);
                            return newSeat;
                        });

                if (seat.isBooked()) {
                    throw new RuntimeException("Seat " + seatNumber + " is already booked.");
                }

                seat.setBooked(true);
                seatRepository.save(seat);

                Booking booking = new Booking();
                booking.setEmail(email);
                booking.setMovie(movie);
                booking.setShowtime(showtime);
                booking.setSeat(seat);
                booking.setNoOfSeats(1);
                bookingRepository.save(booking);
            }

            // Send confirmation email
            sendBookingEmail(email, movie, showtime, seatIds);

            // Pass info to confirmation page
            model.addAttribute("message", "✅ Seats booked successfully: " + String.join(", ", seatIds));
            model.addAttribute("seatIds", seatIds);
            model.addAttribute("showtime", showtime);

        } catch (RuntimeException e) {
            model.addAttribute("message", "❌ Booking failed: " + e.getMessage());
        }

        return "booking-confirmation";
    }

    // 3️⃣ Send booking confirmation email
private void sendBookingEmail(String toEmail, Movie movie, Showtime showtime, List<String> seatIds) {
    String subject = "🎟 Booking Confirmation - " + movie.getTitle();

    String body = "Hello,\n\nYour booking is confirmed!\n\n" +
            "🎬 Movie: " + movie.getTitle() + "\n" +
            "🏛 Theatre: " + showtime.getTheatreName() + "\n" +
            "🕒 Showtime: " + showtime.getShowTime() + "\n" +
            "💺 Seats: " + String.join(", ", seatIds) + "\n\n" +
            "Enjoy your movie!\n\n— Movie Ticket Booking System";

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(subject);
    message.setText(body);

    // ✅ Try-catch block for sending email
    try {
        mailSender.send(message);
        System.out.println("✅ Booking confirmation email sent to " + toEmail);
    } catch (Exception e) {
        System.out.println("❌ Failed to send email: " + e.getMessage());
    }
}
}