package org.rentacar1.app.web.dto;

import lombok.*;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.model.RentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentViewDTO {
    private Car car;
    private BigDecimal totalPrice;
    private RentStatus status;
    private RentPeriod period;
    private LocalDateTime createdOn;
    private LocalDateTime completedOn;
    private LocalDateTime endDate;
}
