package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Users;
import com.paymybuddy.paymybuddy.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public Users getUserById(Long id){
        return usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    }

    public Users saveUser(Users user){
        return usersRepository.save(user);
    }

    public Users getUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    }


    public Users register(Users user){
        if(usersRepository.existsByUsername(user.getUsername())){
            throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé.");
        }
        if(usersRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email est déjà utilisé.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return saveUser(user);
    }
}
