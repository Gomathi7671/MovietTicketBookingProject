package com.example.MovieTicketBookingdemo.service;

import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.model.Showtime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendBookingEmail_Success() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Inception");

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheatreName("PVR Cinemas");
       showtime.setShowTime(LocalTime.of(19, 30));


        List<String> seats = Arrays.asList("A1", "A2", "A3");

        // Capture the sent message
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        emailService.sendBookingEmail("user@example.com", showtime, seats);

        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getSubject().contains("Booking Confirmation"));
        assertTrue(sentMessage.getText().contains("Inception"));
        assertTrue(sentMessage.getText().contains("PVR Cinemas"));
        assertTrue(sentMessage.getText().contains("A1"));
    }
}
