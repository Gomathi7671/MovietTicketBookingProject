package com.example.MovieTicketBookingdemo.Controller;

import java.util.List;
import java.util.Optional;

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

    // ✅ Show all movies (movies.html)
    @GetMapping
    public String showMovies(Model model) {
        model.addAttribute("movies", movieRepo.findAll());
        return "movies"; 
    }

   @GetMapping("/{id}")
public String getMovieDetails(@PathVariable Long id, Model model) {
    Optional<Movie> movieOpt = movieRepo.findById(id);

    if (movieOpt.isPresent()) {
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);

        // Fetch showtimes for this movie
        List<Showtime> showtimes = showtimeRepo.findByMovieId(id);
        model.addAttribute("showtimes", showtimes);

        return "movie-details"; 
    } else {
        model.addAttribute("error", "Movie not found!");
        return "error"; 
    }
}

}
