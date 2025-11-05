package com.example.MovieTicketBookingdemo.Controller;

import java.util.List;


import com.example.MovieTicketBookingdemo.exception.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.MovieTicketBookingdemo.model.Movie;
import com.example.MovieTicketBookingdemo.model.Showtime;
import com.example.MovieTicketBookingdemo.repository.MovieRepository;
import com.example.MovieTicketBookingdemo.repository.ShowtimeRepository;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private ShowtimeRepository showtimeRepo;

    // ✅ Show all movies
    @GetMapping
    public String showMovies(Model model) {
        try {
            List<Movie> movies = movieRepo.findAll();

            if (movies.isEmpty()) {
                throw new MovieNotFoundException("No movies available right now!");
            }

            model.addAttribute("movies", movies);
            return "movies";
        } catch (MovieNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // error.html
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred while fetching movies: " + e.getMessage());
            return "error";
        }
    }

    // ✅ Get specific movie details
    @GetMapping("/{id}")
    public String getMovieDetails(@PathVariable Long id, Model model) {
        try {
            Movie movie = movieRepo.findById(id)
                    .orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found."));

            List<Showtime> showtimes = showtimeRepo.findByMovieId(id);

            model.addAttribute("movie", movie);
            model.addAttribute("showtimes", showtimes);

            return "movie-details";
        } catch (MovieNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred while fetching movie details: " + e.getMessage());
            return "error";
        }
    }
}
