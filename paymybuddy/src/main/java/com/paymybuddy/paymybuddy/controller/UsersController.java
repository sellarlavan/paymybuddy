package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") UserRegistrationDTO dto) {
        userService.register(dto);
        return "redirect:/login";
    }

}
