package com.example.MovieTicketBookingdemo.Config;

import com.example.MovieTicketBookingdemo.model.MovieUser;
import com.example.MovieTicketBookingdemo.model.Role;
import com.example.MovieTicketBookingdemo.repository.MovieUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final MovieUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataLoader(MovieUserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if Admin already exists
        if (userRepository.findByEmail("admin8902@gmail.com") == null) {
            MovieUser admin = new MovieUser();
            admin.setUsername("Super Admin");
            admin.setEmail("admin8902@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // default password
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Admin user created: admin8902@gmail.com / admin123");
        } else {
            System.out.println("ℹ Admin user already exists, skipping creation.");
        }
    }
}
