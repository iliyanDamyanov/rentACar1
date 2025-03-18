package org.rentacar1.app.car.service;

import lombok.extern.slf4j.Slf4j;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {

        this.carRepository = carRepository;
    }

  public Car createCar(Car car){

        if(car.getModel() == null || car.getBrand() == null || car.getType() == null || car.getPricePerWeek() == null){
            throw new IllegalArgumentException("Model, brand, type, and price per week are required.");
        }

      if (car.getPricePerWeek().compareTo(BigDecimal.ZERO) <= 0) {
          throw new IllegalArgumentException("Price per week must be greater than zero.");
      }

      if (carRepository.existsCarByBrandAndModel(car.getBrand(), car.getModel())) {
          throw new IllegalArgumentException("Car with the same brand and model already exists.");
      }

      Car newCar = Car.builder()
              .model(car.getModel())
              .brand(car.getBrand())
              .type(car.getType())
              .pricePerWeek(car.getPricePerWeek())
              .available(true)
              .build();

     return carRepository.save(newCar);

  }





    public Car getCarById(UUID id){
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
    }

    public Car saveCar(Car car){
        return carRepository.save(car);
    }

    public void deleteCar(Car car){
        carRepository.delete(car);
    }


    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue(); // Връща само наличните коли
    }
}
