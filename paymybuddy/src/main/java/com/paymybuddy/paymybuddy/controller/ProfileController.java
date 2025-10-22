package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.UserProfileDTO;
import com.paymybuddy.paymybuddy.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String showProfile(Principal principal, Model model) {
        String email = principal.getName();
        UserProfileDTO dto = profileService.getProfile(email);
        model.addAttribute("user", dto);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("user") UserProfileDTO dto,
                                Principal principal) {
        String currentEmail = principal.getName();
        profileService.updateProfile(currentEmail, dto.getUsername(), dto.getEmail(), dto.getPassword());
        return "redirect:/profile?success";
    }

}
