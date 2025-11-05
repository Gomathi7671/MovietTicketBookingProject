package com.example.MovieTicketBookingdemo.Controller;

import com.example.MovieTicketBookingdemo.dto.CreateShowtimeRequest;
import com.example.MovieTicketBookingdemo.exception.MovieNotFoundException;
import com.example.MovieTicketBookingdemo.exception.MovieUploadException;
import com.example.MovieTicketBookingdemo.exception.ShowtimeCreationException;
import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.model.Showtime;
import com.example.MovieTicketBookingdemo.repository.MovieRepository;
import com.example.MovieTicketBookingdemo.repository.ShowtimeRepository;
import com.example.MovieTicketBookingdemo.service.CloudinaryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    // ✅ Step 1: Show the Upload Form
    @GetMapping("/uploadMovie")
    public String showUploadForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "uploadmovies";
    }

    // ✅ Step 2: Handle Form Submission and Save Movie
    @PostMapping("/uploadMovie")
    public String addMovie(@ModelAttribute Movie movie,
                           @RequestParam("poster") MultipartFile posterFile) {

        if (posterFile.isEmpty()) {
            throw new MovieUploadException("Poster image is required!");
        }

        try {
            // Upload poster to Cloudinary and get back URL
            String imageUrl = cloudinaryService.uploadFile(posterFile);

            // Save the Cloudinary image URL into movie
            movie.setPosterUrl(imageUrl);

            // Save into DB
            movieRepository.save(movie);

            return "redirect:/admin/uploadMovie";
        } catch (Exception e) {
            throw new MovieUploadException("Error while storing movie: " + e.getMessage());
        }
    }

    // ✅ Step 3: Show all movies + scheduling form
    @GetMapping("/manageMovies")
    public String manageMovies(Model model) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("showtimeRequest", new com.example.MovieTicketBookingdemo.dto.CreateShowtimeRequest());
        return "manage-movies";
    }

    // ✅ Step 4: Handle showtime scheduling
    @PostMapping("/scheduleShow")
    public String scheduleShow(@ModelAttribute("showtimeRequest") CreateShowtimeRequest request) {
        try {
            Movie movie = movieRepository.findById(request.getMovieId())
                    .orElseThrow(() -> new MovieNotFoundException("Movie not found with ID: " + request.getMovieId()));

            Showtime showtime = new Showtime();
            showtime.setMovie(movie);
            showtime.setShowDate(request.getShowDate());
            showtime.setShowTime(request.getShowTime());
            showtime.setTheatreName(request.getTheatreName());
            showtime.setCity(request.getCity());
            showtime.setCancellationAvailable(request.isCancellationAvailable());

            showtimeRepository.save(showtime);

            return "redirect:/admin/manageMovies";
        } catch (MovieNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ShowtimeCreationException("Error while scheduling show: " + e.getMessage());
        }
    }
}
