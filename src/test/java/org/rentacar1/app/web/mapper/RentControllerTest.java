package org.rentacar1.app.web.mapper;

import org.junit.jupiter.api.Test;
import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentController.class)
public class RentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentService rentService;

    @Test
    void rentCar_shouldRedirectToMyRents_whenSuccess() throws Exception {
        UUID carId = UUID.randomUUID();
        when(rentService.rentCar(any(), any())).thenReturn(true);

        mockMvc.perform(post("/rent/{carId}", carId)
                        .param("period", RentPeriod.WEEKLY.name())
                        .with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-rents"));
    }

    @Test
    void rentCar_shouldReturnErrorView_whenFailed() throws Exception {
        UUID carId = UUID.randomUUID();
        when(rentService.rentCar(any(), any())).thenReturn(false);

        mockMvc.perform(post("/rent/{carId}", carId)
                        .param("period", RentPeriod.WEEKLY.name())
                        .with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
