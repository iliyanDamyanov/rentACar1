package org.rentacar1.app.web.mapper;

import org.rentacar1.app.rent.model.Rent;
import org.rentacar1.app.rent.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MyRentsController {

    private final RentService rentService;

    @Autowired
    public MyRentsController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/my-rents")
    public ModelAndView getMyRentsPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Rent> userRents = rentService.getUserRents(username);

        ModelAndView modelAndView = new ModelAndView("my-rents");
        modelAndView.addObject("rents", userRents);

        return modelAndView;
    }
    
}
