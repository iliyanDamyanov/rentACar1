package org.rentacar1.app.rent.model;

import jakarta.persistence.*;
import lombok.*;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rents")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RentStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RentPeriod period;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    LocalDateTime completedOn;
}
