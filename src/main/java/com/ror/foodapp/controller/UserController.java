package com.ror.foodapp.controller;

import com.ror.foodapp.dto.UserRegistrationDto;
import com.ror.foodapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(UserRegistrationDto userDto) {
        userService.save(userDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}

