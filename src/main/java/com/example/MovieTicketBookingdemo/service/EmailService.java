package com.example.MovieTicketBookingdemo.service;

import com.example.MovieTicketBookingdemo.model.Showtime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingEmail(String to, Showtime showtime, List<String> seats) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("🎟 Movie Ticket Booking Confirmation");

        String text = "Your booking is confirmed!\n\n" +
                "🎬 Movie: " + showtime.getMovie().getTitle() + "\n" +
                "🏛 Theater: " + showtime.getTheatreName()
+ "\n" +
                "⏰ Time: " + showtime.getShowTime()
+ "\n" +
                "💺 Seats: " + String.join(", ", seats) + "\n\n" +
                "Enjoy your movie!";

        message.setText(text);
        mailSender.send(message);
    }
}
