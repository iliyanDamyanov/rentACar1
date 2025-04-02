package org.rentacar1.app.web.mapper;

import org.junit.jupiter.api.Test;
import org.rentacar1.app.rent.model.Rent;
import org.rentacar1.app.rent.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(MyRentsController.class)
public class MyRentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentService rentService;

    @Test
    void getMyRentsPage_shouldReturnMyRentsViewWithUserRents() throws Exception {

        String username = "testUser";
        List<Rent> fakeRents = Collections.emptyList();
        when(rentService.getUserRents(username)).thenReturn(fakeRents);


        mockMvc.perform(get("/my-rents").with(user(username).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("my-rents"))
                .andExpect(model().attributeExists("rents"));
    }
}
