package com.example.MovieTicketBookingdemo.Controller;

import com.example.MovieTicketBookingdemo.model.MovieUser;
import com.example.MovieTicketBookingdemo.model.Role;
import com.example.MovieTicketBookingdemo.repository.MovieUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class AuthController {

    @Autowired
    private MovieUserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Show register page
    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    // Handle register form
    @PostMapping("/register")
    public String registerUser(@ModelAttribute MovieUser user,
                               @RequestParam("confirm-password") String confirmPassword,
                               Model model) {

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }

        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        // Check if passwords match
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }

        // Encrypt password
        user.setPassword(encoder.encode(user.getPassword()));

        // Default role USER
        user.setRole(Role.USER);

        // Save to DB
        userRepository.save(user);

        // Redirect to login page after successful registration
        return "redirect:/login";
    }

    // Show login page
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // Handle login
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session, // ✅ add HttpSession
                            Model model) {

        MovieUser user = userRepository.findByEmail(email);

        if (user != null && encoder.matches(password, user.getPassword())) {
            // ✅ store user info in session
            session.setAttribute("email", user.getEmail());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            if (user.getRole() == Role.ADMIN) {
                return "admindashboard"; // page for uploading movies
            } else {
                return "redirect:/movies"; // page for users
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // Optional: logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
