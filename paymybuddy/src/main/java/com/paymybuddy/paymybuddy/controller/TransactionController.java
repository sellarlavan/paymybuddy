package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserConnectionService;
import com.paymybuddy.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/transfert")
@RequiredArgsConstructor
public class TransactionController {

    private final UserConnectionService userConnectionService;
    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping
    public String showTransfertPage(Principal principal, Model model) {
        String email = principal.getName();
        model.addAttribute("contacts", userConnectionService.getContacts(email));
        model.addAttribute("transactions", transactionService.getUserTransactions(email));
        model.addAttribute("transaction", new TransactionDTO());

        User user = userService.getUserByEmail(email);
        model.addAttribute("balance", user.getBalance());
        return "transfert";
    }

    @PostMapping
    public String makeTransfert(@ModelAttribute("transaction") TransactionDTO dto, Principal principal) {
        String senderEmail = principal.getName();
        transactionService.makeTransaction(senderEmail, dto);
        return "redirect:/transfert?success";
    }

}
