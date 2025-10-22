package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.paymybuddy.exception.EmailAlreadyExistsOnRegisterException;
import com.paymybuddy.paymybuddy.exception.UserNotFoundException;
import com.paymybuddy.paymybuddy.exception.UsernameAlreadyExistsOnRegisterException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id){
        return usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable."));
    }

    public User getUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable."));
    }

    public User saveUser(User user){
        return usersRepository.save(user);
    }

    public User register(UserRegistrationDTO dto){
        if(usersRepository.existsByUsername(dto.getUsername())){
            throw new UsernameAlreadyExistsOnRegisterException("Le nom d'utilisateur est déjà utilisé.");
        }
        if(usersRepository.existsByEmail(dto.getEmail())){
            throw new EmailAlreadyExistsOnRegisterException("L'email est déjà utilisé.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return saveUser(user);
    }
}
