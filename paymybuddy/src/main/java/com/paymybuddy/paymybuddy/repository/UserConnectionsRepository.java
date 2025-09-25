package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.UserConnectionsId;
import com.paymybuddy.paymybuddy.model.UserConnections;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConnectionsRepository extends JpaRepository<UserConnections, UserConnectionsId> {
}
