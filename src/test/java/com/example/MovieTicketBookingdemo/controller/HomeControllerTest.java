package com.example.MovieTicketBookingdemo.controller;
import com.example.MovieTicketBookingdemo.Controller.HomeController;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHomePageLoadsSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())               // ✅ Expect HTTP 200 OK
                .andExpect(view().name("index"))          // ✅ Expect view name "index"
                .andReturn();

        assertThat(result.getModelAndView()).isNotNull();
        assertThat(result.getModelAndView().getViewName()).isEqualTo("index");
    }

    @Test
    void testHomePageExceptionHandling() throws Exception {
        // Here we simulate what would happen if an exception occurred in the controller.
        // Since the controller's logic is simple, this is just a placeholder demonstration.
        // For a real exception, you'd mock dependencies if they existed.

        // We just verify that the controller doesn't throw unexpected errors.
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())  // should still return 200 OK
                .andExpect(view().name("index"));
    }
}
