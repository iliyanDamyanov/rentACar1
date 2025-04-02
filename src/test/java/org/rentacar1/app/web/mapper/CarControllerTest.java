package org.rentacar1.app.web.mapper;

import org.junit.jupiter.api.Test;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Test
    void getAvailableCars_ShouldReturnCarsViewAndModel() throws Exception {
        List<Car> cars = List.of(new Car(), new Car());
        when(carService.getAvailableCars()).thenReturn(cars);

        mockMvc.perform(get("/cars").with(user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("cars"))
                .andExpect(model().attribute("cars", cars));
    }

    @Test
    void getCarDetails_ShouldRedirectToCars_WhenCarNotFound() throws Exception {
        UUID carId = UUID.randomUUID();
        when(carService.getCarById(carId)).thenReturn(null);

        mockMvc.perform(get("/car-details/" + carId).with(user("testUser").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cars"));
    }

    @Test
    void getCarDetails_ShouldReturnCarDetailsView_WhenCarExists() throws Exception {
        UUID carId = UUID.randomUUID();
        Car car = new Car();
        when(carService.getCarById(carId)).thenReturn(car);

        mockMvc.perform(get("/car-details/" + carId).with(user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("car-details"))
                .andExpect(model().attribute("car", car));
    }


}
