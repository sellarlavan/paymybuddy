package com.paymybuddy.paymybuddy.exception;

import com.paymybuddy.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.paymybuddy.dto.UserProfileDTO;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.ProfileService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserConnectionService;
import com.paymybuddy.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.paymybuddy.paymybuddy.dto.UserRegistrationDTO;

import java.security.Principal;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ProfileService profileService;
    private final UserConnectionService userConnectionService;
    private final TransactionService transactionService;
    private final UserService userService;

    @ExceptionHandler(UsernameAlreadyExistsOnRegisterException.class)
    public String handleUsernameAlreadyExists(UsernameAlreadyExistsOnRegisterException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @ExceptionHandler(EmailAlreadyExistsOnRegisterException.class)
    public String handleEmailAlreadyExistsOnRegister(EmailAlreadyExistsOnRegisterException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "connection";
    }

    @ExceptionHandler(ContactAlreadyExistsException.class)
    public String handleContactAlreadyExists(ContactAlreadyExistsException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "connection";
    }

    @ExceptionHandler(InvalidContactException.class)
    public String handleInvalidContact(InvalidContactException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "connection";
    }

    @ExceptionHandler(UsernameAlreadyExistsOnProfileException.class)
    public String handleUsernameExists(UsernameAlreadyExistsOnProfileException ex, Model model, Principal principal) {
        UserProfileDTO currentDto = profileService.getProfile(principal.getName());
        model.addAttribute("user", currentDto);
        model.addAttribute("error", ex.getMessage());
        return "profile";
    }

    @ExceptionHandler(EmailAlreadyExistsOnProfileException.class)
    public String handleEmailExists(EmailAlreadyExistsOnProfileException ex, Model model, Principal principal) {
        UserProfileDTO currentDto = profileService.getProfile(principal.getName());
        model.addAttribute("user", currentDto);
        model.addAttribute("error", ex.getMessage());
        return "profile";
    }

    @ExceptionHandler(InsuffisantBalanceException.class)
    public String handleInsuffisantBalance(InsuffisantBalanceException ex, Model model, Principal principal) {
        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);

        model.addAttribute("balance", currentUser.getBalance());
        model.addAttribute("contacts", userConnectionService.getContacts(email));
        model.addAttribute("transactions", transactionService.getUserTransactions(email));
        model.addAttribute("transaction", new TransactionDTO());
        model.addAttribute("error", ex.getMessage());

        return "transfert";
    }

    @ExceptionHandler(InvalidTransfertReceiverException.class)
    public String handleInvalidTransfertReceiver(InvalidTransfertReceiverException ex, Model model, Principal principal){
        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);

        model.addAttribute("balance", currentUser.getBalance());
        model.addAttribute("contacts", userConnectionService.getContacts(email));
        model.addAttribute("transactions", transactionService.getUserTransactions(email));
        model.addAttribute("transaction", new TransactionDTO());
        model.addAttribute("error", ex.getMessage());

        return "transfert";
    }

}