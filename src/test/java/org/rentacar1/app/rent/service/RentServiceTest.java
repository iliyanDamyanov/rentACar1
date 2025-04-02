package org.rentacar1.app.rent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.model.CarType;
import org.rentacar1.app.car.repository.CarRepository;
import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.repository.RentRepository;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.repository.UserRepository;
import org.rentacar1.app.wallet.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RentServiceTest {

    @Mock
    private RentRepository rentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private WalletService walletService;

    @InjectMocks
    private RentService rentService;

    private User user;
    private Car car;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser");

        car = Car.builder()
                .id(UUID.randomUUID())
                .brand("Toyota")
                .model("Corolla")
                .type(CarType.CAR)
                .pricePerWeek(BigDecimal.valueOf(300))
                .available(true)
                .build();
    }

    @Test
    void testCalculatePrice_Car_Weekly() {
        BigDecimal basePrice = BigDecimal.valueOf(300);
        BigDecimal result = invokeCalculatePrice(basePrice, CarType.CAR, RentPeriod.WEEKLY);
        assertEquals(basePrice, result);
    }

    @Test
    void testCalculatePrice_Bus_Quarterly() {
        BigDecimal basePrice = BigDecimal.valueOf(300);
        BigDecimal expected = basePrice.multiply(BigDecimal.valueOf(1.3)).multiply(BigDecimal.valueOf(12));
        BigDecimal result = invokeCalculatePrice(basePrice, CarType.BUS, RentPeriod.QUARTERLY);
        assertEquals(expected, result);
    }



    private BigDecimal invokeCalculatePrice(BigDecimal basePrice, CarType carType, RentPeriod period) {
        try {
            var method = RentService.class.getDeclaredMethod("calculatePrice", BigDecimal.class, CarType.class, RentPeriod.class);
            method.setAccessible(true);
            return (BigDecimal) method.invoke(rentService, basePrice, carType, period);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

