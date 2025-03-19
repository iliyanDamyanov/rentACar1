package org.rentacar1.app.web.mapper;

import org.rentacar1.app.rent.model.RentPeriod;
import org.rentacar1.app.rent.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String rentCar(@PathVariable UUID carId, @RequestParam RentPeriod period) {
        boolean success = rentService.rentCar(carId, period);

        if (!success) {
            return "redirect:/car-details/" + carId + "?error=insufficientFundsOrUnavailable";
        }

        return "redirect:/my-rents";
    }
}
