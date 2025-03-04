package org.rentacar1.app.car.service;

import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.repository.CarRepository;

import java.util.List;
import java.util.UUID;

public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
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
