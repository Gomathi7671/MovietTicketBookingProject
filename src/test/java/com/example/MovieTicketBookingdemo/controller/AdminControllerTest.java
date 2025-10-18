package com.example.MovieTicketBookingdemo.controller;
import com.example.MovieTicketBookingdemo.Controller.AdminController;

import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.repository.MovieRepository;
import com.example.MovieTicketBookingdemo.repository.ShowtimeRepository;
import com.example.MovieTicketBookingdemo.service.CloudinaryService;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudinaryService cloudinaryService;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private ShowtimeRepository showtimeRepository;

    // ✅ Test GET /admin/uploadMovie
    @Test
    void testShowUploadForm() throws Exception {
        mockMvc.perform(get("/admin/uploadMovie"))
                .andExpect(status().isOk());
    }

    // ✅ Test POST /admin/uploadMovie
    @Test
    void testAddMovie() throws Exception {
        MockMultipartFile poster = new MockMultipartFile(
                "poster", "poster.jpg", MediaType.IMAGE_JPEG_VALUE, "dummy content".getBytes()
        );

        when(cloudinaryService.uploadFile(any())).thenReturn("http://dummyurl.com/poster.jpg");
        when(movieRepository.save(any(Movie.class))).thenReturn(new Movie());

        mockMvc.perform(multipart("/admin/uploadMovie")
                        .file(poster)
                        .param("title", "Dummy Movie")
                        .param("description", "Dummy Description")
                        .param("genre", "Action")
                        .param("duration", "120 min")
                        .param("rating", "8.5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/uploadMovie"));
    }

    // ✅ Test GET /admin/manageMovies
    @Test
    void testManageMovies() throws Exception {
        mockMvc.perform(get("/admin/manageMovies"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("showtimeRequest"));
    }

    // ✅ Test POST /admin/scheduleShow
    @Test
    void testScheduleShow() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        mockMvc.perform(post("/admin/scheduleShow")
                        .param("movieId", "1")
                        .param("showDate", LocalDate.now().toString())
                        .param("showTime", LocalTime.now().toString())
                        .param("theatreName", "ABC Theatre")
                        .param("city", "Mumbai")
                        .param("cancellationAvailable", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/uploadMovie"));
    }
}
