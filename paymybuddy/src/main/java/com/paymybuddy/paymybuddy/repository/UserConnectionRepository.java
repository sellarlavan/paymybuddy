package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.UserConnection;
import com.paymybuddy.paymybuddy.model.UserConnectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, UserConnectionId> {

    List<UserConnection> findByUser(User user);
    boolean existsByUserAndContact(User user, User contact);
    boolean existsById(UserConnectionId id);
    List<UserConnection> findAllByUser(User user);
}