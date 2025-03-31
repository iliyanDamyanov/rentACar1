package org.rentacar1.app.web.mapper;


import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.repository.UserRepository;
import org.rentacar1.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {


    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("/admin/users")
    public String viewAllUsers(Model model){
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/admin/delete/{userId}")
    public String deleteUser(@PathVariable UUID userId) {
        userRepository.deleteById(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/edit-user/{id}")
    public String showEditUserForm(@PathVariable("id") UUID id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/admin/update")
    public String updateUser(User user) {
        userRepository.save(user);
        return "redirect:/admin/users";
    }

}
