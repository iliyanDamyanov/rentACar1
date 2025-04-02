package org.rentacar1.app.web.mapper;

import org.junit.jupiter.api.Test;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.model.RentStatus;
import org.rentacar1.app.rent.service.RentService;
import org.rentacar1.app.web.dto.RentViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        // Arrange
        String username = "testUser";

        RentViewDTO rent = RentViewDTO.builder()
                .car(Car.builder().brand("Toyota").model("Yaris").build())
                .totalPrice(BigDecimal.valueOf(200))
                .status(RentStatus.ACTIVE)
                .period(RentPeriod.WEEKLY)
                .createdOn(LocalDateTime.now())
                .completedOn(null)
                .endDate(LocalDateTime.now().plusWeeks(1))
                .build();

        List<RentViewDTO> fakeRents = List.of(rent);
        when(rentService.getUserRentDTOs(username)).thenReturn(fakeRents);

        // Act + Assert
        mockMvc.perform(get("/my-rents").with(user(username).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("my-rents"))
                .andExpect(model().attributeExists("rents"));
    }
}
