package org.rentacar1.app.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getContactPage_shouldReturnContactView() throws Exception {
        mockMvc.perform(get("/contact")
                        .with(user("testUser").password("123456").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));
    }

    @Test
    void postContactForm_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "Hello there!")
                        .with(csrf())
                        .with(user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("successMessage", "Your message has been sent successfully!"));
    }
}
