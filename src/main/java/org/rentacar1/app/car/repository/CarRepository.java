package org.rentacar1.app.car.repository;

import org.rentacar1.app.car.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    boolean existsCarByBrandAndModel(String brand, String model);

    List<Car> findByAvailableTrue();

}
