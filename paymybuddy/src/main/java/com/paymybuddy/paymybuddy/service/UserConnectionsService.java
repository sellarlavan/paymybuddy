package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.UserConnections;
import com.paymybuddy.paymybuddy.model.UserConnectionsId;
import com.paymybuddy.paymybuddy.model.Users;
import com.paymybuddy.paymybuddy.repository.UserConnectionsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserConnectionsService {

    private final UserConnectionsRepository userConnectionsRepository;
    private final UsersService usersService;

    public UserConnections addContactByEmail(Long userId, String contactEmail) {
        Users user = usersService.getUserById(userId);
        Users contact = usersService.getUserByEmail(contactEmail);

        if (user.equals(contact)) {
            throw new IllegalArgumentException("Impossible de s'ajouter soi-même.");
        }

        UserConnectionsId id = new UserConnectionsId(user.getId(), contact.getId());
        if (userConnectionsRepository.existsById(id)) {
            throw new IllegalArgumentException("Ce contact est déjà ajouté.");
        }

        UserConnections connection = new UserConnections(user, contact);
        return userConnectionsRepository.save(connection);
    }
}
