package org.rentacar1.app.car.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.model.CarType;
import org.rentacar1.app.car.repository.CarRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void testCreateCar_ShouldSaveAndReturnCar_WhenValid() {
        when(carRepository.existsCarByBrandAndModel(car.getBrand(), car.getModel())).thenReturn(false);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car created = carService.createCar(car);

        assertNotNull(created);
        assertEquals(car.getBrand(), created.getBrand());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testCreateCar_ShouldThrowException_WhenMissingFields() {
        car.setModel(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                carService.createCar(car));

        assertTrue(exception.getMessage().contains("required"));
    }

    @Test
    void testCreateCar_ShouldThrowException_WhenPriceInvalid() {
        car.setPricePerWeek(BigDecimal.ZERO);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                carService.createCar(car));

        assertTrue(exception.getMessage().contains("greater than zero"));
    }

    @Test
    void testCreateCar_ShouldThrowException_WhenCarExists() {
        when(carRepository.existsCarByBrandAndModel(car.getBrand(), car.getModel())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                carService.createCar(car));

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testGetCarById_ShouldReturnCar_WhenFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        Car found = carService.getCarById(car.getId());

        assertNotNull(found);
        assertEquals(car.getId(), found.getId());
    }

    @Test
    void testGetCarById_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                carService.getCarById(id));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testSaveCar_ShouldReturnSavedCar() {
        when(carRepository.save(car)).thenReturn(car);

        Car saved = carService.saveCar(car);

        assertNotNull(saved);
        assertEquals(car.getModel(), saved.getModel());
    }

    @Test
    void testDeleteCar_ShouldCallDelete() {
        carService.deleteCar(car);

        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void testGetAvailableCars_ShouldReturnAvailableCars() {
        List<Car> cars = Arrays.asList(car);
        when(carRepository.findByAvailableTrue()).thenReturn(cars);

        List<Car> result = carService.getAvailableCars();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }
}
