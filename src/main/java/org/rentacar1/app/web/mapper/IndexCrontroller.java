package org.rentacar1.app.web.mapper;

import jakarta.validation.Valid;
import org.rentacar1.app.car.model.Car;
import org.rentacar1.app.car.service.CarService;
import org.rentacar1.app.security.AuthenticationMetadata;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.service.UserService;
import org.rentacar1.app.web.dto.LoginRequest;
import org.rentacar1.app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexCrontroller {

    private final UserService userService;
    private final CarService carService;

    @Autowired
    public IndexCrontroller(UserService userService, CarService carService) {
        this.userService = userService;
        this.carService = carService;
    }

    @GetMapping("/")
    public ModelAndView indexPage() {

        ModelAndView modelAndView = new ModelAndView("index");
        List <Car> availableCars = carService.getAvailableCars();
        modelAndView.addObject("availableCars", availableCars);


        return modelAndView;
    }


    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        userService.register(registerRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login"); // Използваме viewName login за login.html
        modelAndView.addObject("loginRequest", new LoginRequest());

        if (errorParam != null) { // Проверяваме дали има грешка при логин
            modelAndView.addObject("errorMessage", "Incorrect username or password!"); // Подаваме съобщението към view-то
        }


        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");

        if (authenticationMetadata == null) {
            // Ако потребителят не е автентикиран, го пренасочваме към страницата за логин
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        User user = userService.getById(authenticationMetadata.getUserId());
        modelAndView.addObject("user", user);

        return modelAndView;
    }





}
