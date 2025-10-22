package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.UserProfileDTO;
import com.paymybuddy.paymybuddy.exception.EmailAlreadyExistsOnProfileException;
import com.paymybuddy.paymybuddy.exception.UserNotFoundOnProfileException;
import com.paymybuddy.paymybuddy.exception.UsernameAlreadyExistsOnProfileException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserProfileDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundOnProfileException("L'utilisateur est introuvable."));
        return new UserProfileDTO(user.getUsername(), user.getEmail(), "");
    }

    @Transactional
    public void updateProfile(String currentEmail, String newUsername, String newEmail, String newPassword) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UserNotFoundOnProfileException("L'utilisateur est introuvable."));

        if (!user.getUsername().equals(newUsername)
                && userRepository.existsByUsername(newUsername)) {
            throw new UsernameAlreadyExistsOnProfileException("Le nom d'utilisateur est déjà utilisé.");
        }

        if (!user.getEmail().equals(newEmail)
                && userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsOnProfileException("L'email est déjà utilisé.");
        }

        user.setUsername(newUsername);
        user.setEmail(newEmail);

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
