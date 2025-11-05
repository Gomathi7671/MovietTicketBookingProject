package com.example.MovieTicketBookingdemo.Controller;

import com.example.MovieTicketBookingdemo.exception.InvalidCredentialsException;
import com.example.MovieTicketBookingdemo.exception.PasswordMismatchException;
import com.example.MovieTicketBookingdemo.exception.UserAlreadyExistsException;
import com.example.MovieTicketBookingdemo.model.MovieUser;
import com.example.MovieTicketBookingdemo.model.Role;
import com.example.MovieTicketBookingdemo.repository.MovieUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private MovieUserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ✅ Show register page
    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    // ✅ Handle register form
    @PostMapping("/register")
    public String registerUser(@ModelAttribute MovieUser user,
                               @RequestParam("confirm-password") String confirmPassword) {

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already exists!");
        }

        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already exists!");
        }

        // Check password confirmation
        if (!user.getPassword().equals(confirmPassword)) {
            throw new PasswordMismatchException("Passwords do not match!");
        }

        // Encrypt password and assign default role
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        // Save user
        userRepository.save(user);

        // Redirect to login page
        return "redirect:/login";
    }

    // ✅ Show login page
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // ✅ Handle login
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session) {

        MovieUser user = userRepository.findByEmail(email);

        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Store user info in session
        session.setAttribute("email", user.getEmail());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        // Redirect based on role
        if (user.getRole() == Role.ADMIN) {
            return "admindashboard"; // admin page
        } else {
            return "redirect:/movies"; // user movie listing
        }
    }

    // ✅ Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
