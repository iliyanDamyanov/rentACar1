package org.rentacar1.app.rent.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.model.CarType;
import org.rentacar1.app.car.repository.CarRepository;
import org.rentacar1.app.notification.model.Notification;
import org.rentacar1.app.notification.service.NotificationService;
import org.rentacar1.app.rent.model.Rent;
import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.model.RentStatus;
import org.rentacar1.app.rent.repository.RentRepository;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.repository.UserRepository;
import org.rentacar1.app.wallet.service.WalletService;
import org.rentacar1.app.web.dto.NotificationDTO;
import org.rentacar1.app.web.dto.RentViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RentService {
    public final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final WalletService walletService;
    private final NotificationService notificationService;

    @Autowired
    public RentService(RentRepository rentRepository, UserRepository userRepository, CarRepository carRepository, WalletService walletService, NotificationService notificationService) {

        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.walletService = walletService;
        this.notificationService = notificationService;
    }

    public boolean rentCar(UUID carId, RentPeriod period) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));


        if (!car.isAvailable()) {
            log.warn("Car {} is already rented!", carId);
            return false;
        }


        BigDecimal basePrice = car.getPricePerWeek();
        BigDecimal finalPrice = calculatePrice(basePrice, car.getType(), period);


        if (!walletService.hasSufficientFunds(user, finalPrice)) {
            log.warn("User {} does not have enough balance!", user.getUsername());
            return false;
        }


        walletService.deductAmount(user, finalPrice);


        Rent rent = Rent.builder()
                .user(user)
                .car(car)
                .status(RentStatus.ACTIVE)
                .period(period)
                .price(finalPrice)
                .totalPrice(finalPrice)
                .createdOn(LocalDateTime.now())
                .completedOn(null)
                .build();


        car.setAvailable(false);
        carRepository.save(car);
        rentRepository.save(rent);

        String formattedPrice = String.format("%.2f", finalPrice);
        String message = "Successfully rented " + car.getModel() + " for $" + formattedPrice + ".";

        Notification notification = Notification.builder()
                .recipient(user.getUsername())
                .message(message)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        notificationService.createNotification(notification);

        notificationService.createNotification(notification);
        log.info("User {} successfully rented car {} for {}", user.getUsername(), carId, period);
        return true;
    }

    private BigDecimal calculatePrice(BigDecimal basePrice, CarType carType, RentPeriod period) {
        BigDecimal multiplier = switch (period) {
            case WEEKLY -> BigDecimal.ONE;
            case MONTHLY -> BigDecimal.valueOf(4);
            case QUARTERLY -> BigDecimal.valueOf(12);
            case YEARLY -> BigDecimal.valueOf(52);
        };

        if (carType == CarType.BUS) {
            basePrice = basePrice.multiply(BigDecimal.valueOf(1.3));
        }

        return basePrice.multiply(multiplier);
    }

    public List<RentViewDTO> getUserRentDTOs(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Rent> rents = rentRepository.findByUser(user);

        return rents.stream()
                .map(rent -> {
                    LocalDateTime endDate = switch (rent.getPeriod()) {
                        case WEEKLY -> rent.getCreatedOn().plusWeeks(1);
                        case MONTHLY -> rent.getCreatedOn().plusMonths(1);
                        case QUARTERLY -> rent.getCreatedOn().plusMonths(3);
                        case YEARLY -> rent.getCreatedOn().plusYears(1);
                    };

                    return RentViewDTO.builder()
                            .car(rent.getCar())
                            .totalPrice(rent.getTotalPrice())
                            .status(rent.getStatus())
                            .period(rent.getPeriod())
                            .createdOn(rent.getCreatedOn())
                            .completedOn(rent.getCompletedOn())
                            .endDate(endDate)
                            .build();
                }).toList();
    }

    private void sendCarRentalNotification(User user, String carName) {
        String notificationUrl = "http://localhost:8081/api/notifications";

        NotificationDTO notificationDTO = new NotificationDTO(
                user.getId(),
                user.getUsername(),
                "Успешно наемане на кола: " + carName,
                false,
                LocalDateTime.now()
        );

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(notificationUrl, notificationDTO, Void.class);
            log.info("Successfully sent car rental notification for user [{}]", user.getUsername());
        } catch (Exception e) {
            log.error("Failed to send car rental notification for user [{}]: {}", user.getUsername(), e.getMessage());
        }
    }

}
