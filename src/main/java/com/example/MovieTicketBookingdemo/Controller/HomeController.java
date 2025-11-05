package com.example.MovieTicketBookingdemo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home(Model model) {
        try {
            logger.info("Accessing home page...");
            return "index"; // ✅ index.html in templates folder
        } catch (Exception e) {
            logger.error("Error loading home page: {}", e.getMessage());
            model.addAttribute("error", "Unable to load home page right now. Please try again later.");
            return "error"; // Fallback error.html
        }
    }
}
