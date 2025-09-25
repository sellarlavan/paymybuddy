package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.Transactions;
import com.paymybuddy.paymybuddy.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionsService transactionsService;

    @PostMapping
    public ResponseEntity<Transactions> createTransaction(@RequestBody Transactions transaction) {
        Transactions savedTransaction = transactionsService.createTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<Transactions>> getTransactionsBySender(@PathVariable Long senderId) {
        return ResponseEntity.ok(transactionsService.getTransactionsBySender(senderId));
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<Transactions>> getTransactionsByReceiver(@PathVariable Long receiverId) {
        return ResponseEntity.ok(transactionsService.getTransactionsByReceiver(receiverId));
    }
}
