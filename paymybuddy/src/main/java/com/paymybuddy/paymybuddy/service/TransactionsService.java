package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Transactions;
import com.paymybuddy.paymybuddy.model.Users;
import com.paymybuddy.paymybuddy.repository.TransactionsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final UsersService usersService;

    public Transactions createTransaction(Transactions transaction) {

        if (transaction.getSender() == null || transaction.getReceiver() == null) {
            throw new IllegalArgumentException("Un émetteur et un destinataire sont requis.");
        }

        Users sender = usersService.getUserById(transaction.getSender().getId());
        Users receiver = usersService.getUserById(transaction.getReceiver().getId());

        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        if (sender.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new IllegalArgumentException("Solde insuffisant.");
        }

        sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));

        usersService.saveUser(sender);
        usersService.saveUser(receiver);

        transaction.setSender(sender);
        transaction.setReceiver(receiver);

        return transactionsRepository.save(transaction);
    }

    public List<Transactions> getTransactionsBySender(Long senderId) {
        return transactionsRepository.findBySenderId(senderId);
    }

    public List<Transactions> getTransactionsByReceiver(Long receiverId) {
        return transactionsRepository.findByReceiverId(receiverId);
    }
}
