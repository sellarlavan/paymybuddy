package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.exception.ContactAlreadyExistsException;
import com.paymybuddy.paymybuddy.exception.InvalidContactException;
import com.paymybuddy.paymybuddy.exception.UserNotFoundException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.UserConnection;
import com.paymybuddy.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;
    private final UserRepository userRepository;

    public void addContact(String currentUserEmail, String contactEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable."));

        User contact = userRepository.findByEmail(contactEmail)
                .orElseThrow(() -> new UserNotFoundException("Contact introuvable."));

        if (currentUser.getId().equals(contact.getId())) {
            throw new InvalidContactException("Impossible de s'ajouter soi-même comme contact.");
        }

        if (userConnectionRepository.existsByUserAndContact(currentUser, contact)) {
            throw new ContactAlreadyExistsException("Ce contact est déjà dans vos contacts.");
        }

        UserConnection connection = new UserConnection(currentUser, contact);
        userConnectionRepository.save(connection);
    }

    public List<User> getContacts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable."));

        return userConnectionRepository.findAllByUser(user)
                .stream()
                .map(UserConnection::getContact)
                .toList();
    }
}