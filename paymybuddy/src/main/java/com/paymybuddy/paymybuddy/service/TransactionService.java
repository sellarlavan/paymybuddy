package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.dto.TransactionViewDTO;
import com.paymybuddy.paymybuddy.exception.InsuffisantBalanceException;
import com.paymybuddy.paymybuddy.exception.InvalidTransfertReceiverException;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.paymybuddy.paymybuddy.repository.UserConnectionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void makeTransaction(String senderEmail, TransactionDTO dto) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new InvalidTransfertReceiverException("Utilisateur introuvable."));

        User receiver = userRepository.findByEmail(dto.getReceiverEmail())
                .orElseThrow(() -> new InvalidTransfertReceiverException("Contact introuvable."));

        if (!userConnectionRepository.existsByUserAndContact(sender, receiver)) {
            throw new InvalidTransfertReceiverException("Le destinataire n’est pas dans vos contacts.");
        }

        if (sender.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new InsuffisantBalanceException("Votre solde est insuffisant.");
        }

        sender.setBalance(sender.getBalance().subtract(dto.getAmount()));
        receiver.setBalance(receiver.getBalance().add(dto.getAmount()));

        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .build();

        transactionRepository.save(transaction);
    }

    public List<TransactionViewDTO> getUserTransactions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Transaction> transactions = transactionRepository.findBySenderOrReceiverOrderByIdDesc(user, user);

        List<TransactionViewDTO> result = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getSender().equals(user)) {
                result.add(new TransactionViewDTO(
                        transaction.getReceiver().getUsername(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        true
                ));
            } else {
                result.add(new TransactionViewDTO(
                        transaction.getSender().getUsername(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        false
                ));
            }
        }
        return result;
    }
}