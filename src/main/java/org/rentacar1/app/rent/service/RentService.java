package org.rentacar1.app.rent.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.model.CarType;
import org.rentacar1.app.car.repository.CarRepository;
import org.rentacar1.app.rent.model.Rent;
import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.model.RentStatus;
import org.rentacar1.app.rent.repository.RentRepository;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class RentService {
    public final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Autowired
    public RentService(RentRepository rentRepository, UserRepository userRepository, CarRepository carRepository) {

        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    public Rent createRent(UUID userId, UUID carId, RentPeriod period) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available for rent");
        }

        // Изчисляване на цената
        BigDecimal basePrice = car.getPricePerWeek(); // Базова цена (седмична)
        BigDecimal finalPrice = calculatePrice(basePrice, car.getType(), period);

        // Създаване на Rent запис
        Rent rent = Rent.builder()
                .user(user)
                .car(car)
                .status(RentStatus.ACTIVE)
                .period(period)
                .price(finalPrice)
                .totalPrice(finalPrice)
                .createdOn(LocalDateTime.now())
                .build();

        // Маркиране на колата като наета
        car.setAvailable(false);
        carRepository.save(car);

        return rentRepository.save(rent);
    }

    private BigDecimal calculatePrice(BigDecimal basePrice, CarType carType, RentPeriod period) {
        BigDecimal multiplier = switch (period) {
            case WEEKLY -> BigDecimal.ONE;
            case MONTHLY -> BigDecimal.valueOf(4);
            case QUARTERLY -> BigDecimal.valueOf(12);
            case YEARLY -> BigDecimal.valueOf(52);
        };

        if (carType == CarType.BUS) {
            basePrice = basePrice.multiply(BigDecimal.valueOf(1.3)); // +30% за автобуси
        }

        return basePrice.multiply(multiplier);
    }



}
