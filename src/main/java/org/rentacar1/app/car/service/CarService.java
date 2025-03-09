package org.rentacar1.app.car.service;

import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.repository.CarRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class CarService {
    private final CarRepository carRepository;

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


}
