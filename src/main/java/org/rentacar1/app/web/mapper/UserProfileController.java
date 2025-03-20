package org.rentacar1.app.web.mapper;

import org.rentacar1.app.security.AuthenticationMetadata;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.service.UserService;
import org.rentacar1.app.web.dto.UpdateProfileRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserProfileController {


    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile")
    public ModelAndView getProfilePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata){
        User user = userService.getById(authenticationMetadata.getUserId()); // Извличаме User от базата

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("updateProfileRequest", new UpdateProfileRequest());

        return modelAndView;
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                UpdateProfileRequest updateProfileRequest) {

        userService.updateProfile(authenticationMetadata.getUserId(), updateProfileRequest);
        return "redirect:/home";
    }
}
