package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository <Transactions, Long> {

    List<Transactions> findBySenderId(Long senderId);

    List<Transactions> findByReceiverId(Long receiverId);
}
