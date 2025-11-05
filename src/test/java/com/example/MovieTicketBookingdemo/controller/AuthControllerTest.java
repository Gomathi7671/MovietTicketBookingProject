package com.example.MovieTicketBookingdemo.controller;
import com.example.MovieTicketBookingdemo.Controller.AuthController;

import com.example.MovieTicketBookingdemo.exception.InvalidCredentialsException;
import com.example.MovieTicketBookingdemo.exception.PasswordMismatchException;
import com.example.MovieTicketBookingdemo.exception.UserAlreadyExistsException;
import com.example.MovieTicketBookingdemo.model.MovieUser;
import com.example.MovieTicketBookingdemo.model.Role;
import com.example.MovieTicketBookingdemo.repository.MovieUserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private MovieUserRepository userRepository;

    @Mock
    private HttpSession session;

    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder();
    }

    // ✅ Test: Register new user success
    @Test
    void testRegisterUser_Success() {
        MovieUser user = new MovieUser();
        user.setEmail("newuser@gmail.com");
        user.setUsername("newuser");
        user.setPassword("1234");

        when(userRepository.findByEmail("newuser@gmail.com")).thenReturn(null);
        when(userRepository.findByUsername("newuser")).thenReturn(null);

        String view = authController.registerUser(user, "1234");

        verify(userRepository).save(any(MovieUser.class));
        assertEquals("redirect:/login", view);
    }

    // ❌ Test: Email already exists
    @Test
    void testRegisterUser_EmailExists_ThrowsException() {
        MovieUser user = new MovieUser();
        user.setEmail("existing@gmail.com");
        user.setUsername("someone");
        user.setPassword("pass");

        when(userRepository.findByEmail("existing@gmail.com")).thenReturn(new MovieUser());

        assertThrows(UserAlreadyExistsException.class, () ->
                authController.registerUser(user, "pass")
        );
    }

    // ❌ Test: Username already exists
    @Test
    void testRegisterUser_UsernameExists_ThrowsException() {
        MovieUser user = new MovieUser();
        user.setEmail("unique@gmail.com");
        user.setUsername("takenname");
        user.setPassword("pass");

        when(userRepository.findByEmail("unique@gmail.com")).thenReturn(null);
        when(userRepository.findByUsername("takenname")).thenReturn(new MovieUser());

        assertThrows(UserAlreadyExistsException.class, () ->
                authController.registerUser(user, "pass")
        );
    }

    // ❌ Test: Passwords don’t match
    @Test
    void testRegisterUser_PasswordMismatch_ThrowsException() {
        MovieUser user = new MovieUser();
        user.setEmail("unique@gmail.com");
        user.setUsername("unique");
        user.setPassword("pass1");

        when(userRepository.findByEmail("unique@gmail.com")).thenReturn(null);
        when(userRepository.findByUsername("unique")).thenReturn(null);

        assertThrows(PasswordMismatchException.class, () ->
                authController.registerUser(user, "pass2")
        );
    }

    // ✅ Test: Login success (USER role)
    @Test
    void testLoginUser_Success_UserRole() {
        MovieUser user = new MovieUser();
        user.setEmail("user@gmail.com");
        user.setPassword(encoder.encode("password"));
        user.setRole(Role.USER);
        user.setUsername("testuser");

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(user);

        String view = authController.loginUser("user@gmail.com", "password", session);

        verify(session).setAttribute("email", "user@gmail.com");
        verify(session).setAttribute("username", "testuser");
        verify(session).setAttribute("role", Role.USER);
        assertEquals("redirect:/movies", view);
    }

    // ✅ Test: Login success (ADMIN role)
    @Test
    void testLoginUser_Success_AdminRole() {
        MovieUser user = new MovieUser();
        user.setEmail("admin@gmail.com");
        user.setPassword(encoder.encode("adminpass"));
        user.setRole(Role.ADMIN);
        user.setUsername("admin");

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(user);

        String view = authController.loginUser("admin@gmail.com", "adminpass", session);

        verify(session).setAttribute("email", "admin@gmail.com");
        verify(session).setAttribute("username", "admin");
        verify(session).setAttribute("role", Role.ADMIN);
        assertEquals("admindashboard", view);
    }

    // ❌ Test: Invalid login credentials
    @Test
    void testLoginUser_InvalidCredentials_ThrowsException() {
        when(userRepository.findByEmail("wrong@gmail.com")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () ->
                authController.loginUser("wrong@gmail.com", "password", session)
        );
    }

    // ✅ Test: Logout
    @Test
    void testLogout() {
        String view = authController.logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/login", view);
    }
}
