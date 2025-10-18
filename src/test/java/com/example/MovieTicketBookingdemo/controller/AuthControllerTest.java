package com.example.MovieTicketBookingdemo.controller;
import com.example.MovieTicketBookingdemo.Controller.AuthController;

import com.example.MovieTicketBookingdemo.model.MovieUser;
import com.example.MovieTicketBookingdemo.model.Role;
import com.example.MovieTicketBookingdemo.repository.MovieUserRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieUserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ✅ Test GET /register
    @Test
    void testShowRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    // ✅ Test successful registration
    @Test
    void testRegisterUserSuccess() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.save(any(MovieUser.class))).thenReturn(new MovieUser());

        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("confirm-password", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // ✅ Test registration with existing email
    @Test
    void testRegisterUserEmailExists() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(new MovieUser());

        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("username", "newuser")
                        .param("password", "password123")
                        .param("confirm-password", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("register"));
    }

    // ✅ Test registration with non-matching passwords
    @Test
    void testRegisterUserPasswordMismatch() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("confirm-password", "wrongpassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("register"));
    }

    // ✅ Test GET /login
    @Test
    void testShowLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // ✅ Test successful login for USER
    @Test
    void testLoginUserSuccess() throws Exception {
        MovieUser user = new MovieUser();
        user.setEmail("user@example.com");
        user.setUsername("user");
        user.setPassword(encoder.encode("password123"));
        user.setRole(Role.USER);

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/login")
                        .param("email", "user@example.com")
                        .param("password", "password123")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));
    }

    // ✅ Test login with invalid credentials
    @Test
    void testLoginUserInvalid() throws Exception {
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("email", "invalid@example.com")
                        .param("password", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("login"));
    }

    // ✅ Test GET /logout
    @Test
    void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", "user@example.com");

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
