package org.rentacar1.app.web.mapper;

import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/rent")
public class RentController {

    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping("/{carId}")
    public ModelAndView rentCar(@PathVariable UUID carId,
                                @RequestParam RentPeriod period) {
        try {
            boolean success = rentService.rentCar(carId, period);

            if (success) {
                return new ModelAndView("redirect:/my-rents");
            } else {
                ModelAndView modelAndView = new ModelAndView("error");
                modelAndView.addObject("errorMessage", "Unable to rent the car. Insufficient funds or car is unavailable.");
                return modelAndView;
            }
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("errorMessage", e.getMessage());
            return modelAndView;
        }
    }
}
