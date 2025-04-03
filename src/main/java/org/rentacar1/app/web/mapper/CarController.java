package org.rentacar1.app.web.mapper;

import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public String getAvailableCars(Model model) {
        List<Car> availableCars = carService.getAvailableCars();
        model.addAttribute("cars", availableCars);
        return "cars";
    }

    @GetMapping("/car-details/{id}")
    public String getCarDetails(@PathVariable UUID id, Model model) {
        Optional<Car> car = Optional.ofNullable(carService.getCarById(id));
        if (car.isEmpty()) {
            return "redirect:/cars";
        }
        model.addAttribute("car", car.get());
        return "car-details";
    }



}
