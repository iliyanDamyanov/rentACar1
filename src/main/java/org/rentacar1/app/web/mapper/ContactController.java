package org.rentacar1.app.web.mapper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @GetMapping
    public String showContactPage(){

        return "contact";
    }


    @PostMapping
    public ModelAndView submitContactForm(@RequestParam String name,
                                          @RequestParam String email,
                                          @RequestParam String message) {

        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Message: " + message);

        ModelAndView modelAndView = new ModelAndView("contact");
        modelAndView.addObject("successMessage", "Your message has been sent successfully!");
        return modelAndView;
    }
}
