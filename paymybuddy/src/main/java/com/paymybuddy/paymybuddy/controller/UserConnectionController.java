package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.service.UserConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/connection")
@RequiredArgsConstructor
public class UserConnectionController {

    private final UserConnectionService userConnectionService;

    @GetMapping
    public String showConnectionForm() {
        return "connection";
    }

    @PostMapping("/add")
    public String addContact(@RequestParam("contactEmail") String contactEmail, Authentication authentication, Model model) {
        userConnectionService.addContact(authentication.getName(), contactEmail);
        model.addAttribute("success", contactEmail + " a été ajouté en relation.");
        return "connection";
    }

}
