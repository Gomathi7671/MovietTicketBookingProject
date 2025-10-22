package com.example.MovieTicketBookingdemo.Controller;

import com.example.MovieTicketBookingdemo.dto.CreateShowtimeRequest;
import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.model.Showtime;
import com.example.MovieTicketBookingdemo.repository.MovieRepository;
import com.example.MovieTicketBookingdemo.repository.ShowtimeRepository;
import com.example.MovieTicketBookingdemo.service.CloudinaryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return "uploadmovies"; // uploadmovies.html
    }

    // ✅ Step 2: Handle Form Submission and Save Movie
    @PostMapping("/uploadMovie")
    public String addMovie(@ModelAttribute Movie movie,
                           @RequestParam("poster") MultipartFile posterFile,
                           Model model) {
        try {
            // Upload poster to Cloudinary and get back URL
            String imageUrl = cloudinaryService.uploadFile(posterFile);

            // Save the Cloudinary image URL into movie
            movie.setPosterUrl(imageUrl);

            // Save into DB
            movieRepository.save(movie);

            model.addAttribute("message", "Movie added successfully!");
            return "redirect:/admin/uploadMovie"; // success.html
        } catch (Exception e) {
            model.addAttribute("error", "Error while storing movie: " + e.getMessage());
            return "redirect:/admin/uploadMovie"; // error.html
        }
    }

    // ✅ Step 3: Show all movies + scheduling form
    @GetMapping("/manageMovies")
    public String manageMovies(Model model) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("showtimeRequest", new CreateShowtimeRequest());
        return "manage-movies"; // manage-movies.html
    }

    // ✅ Step 4: Handle showtime scheduling (no Lombok builder)
    @PostMapping("/scheduleShow")
    public String scheduleShow(@ModelAttribute("showtimeRequest") CreateShowtimeRequest request, Model model) {
        try {
            Movie movie = movieRepository.findById(request.getMovieId())
                    .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

            Showtime showtime = new Showtime();
            showtime.setMovie(movie);
            showtime.setShowDate(request.getShowDate());
            showtime.setShowTime(request.getShowTime());
            showtime.setTheatreName(request.getTheatreName());
            showtime.setCity(request.getCity());
            showtime.setCancellationAvailable(request.isCancellationAvailable());

            showtimeRepository.save(showtime);

            model.addAttribute("message", "Showtime added successfully!");
            return "redirect:/admin/uploadMovie"; // success.html
        } catch (Exception e) {
            model.addAttribute("error", "Error while scheduling: " + e.getMessage());
            return "redirect:/admin/uploadMovie"; // error.html
        }
    }
}
