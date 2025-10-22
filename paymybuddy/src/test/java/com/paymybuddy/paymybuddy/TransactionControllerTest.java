package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.UserConnection;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private User user2;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
        userConnectionRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setBalance(BigDecimal.valueOf(100));
        userRepository.save(user);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setBalance(BigDecimal.valueOf(50));
        userRepository.save(user2);

        userConnectionRepository.save(new UserConnection(user, user2));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testTransfertPageShowsContacts() throws Exception {
        mockMvc.perform(get("/transfert"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfert"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attribute("contacts",
                        org.hamcrest.Matchers.hasItem(
                                org.hamcrest.Matchers.hasProperty("email", org.hamcrest.Matchers.equalTo("user2@gmail.com"))
                        )));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testSuccessfulTransaction() throws Exception {
        mockMvc.perform(post("/transfert")
                        .param("receiverEmail", "user2@gmail.com")
                        .param("amount", "20.00")
                        .param("description", "test payment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfert?success"));

        User userUpdated = userRepository.findByEmail("user@gmail.com").orElseThrow();
        User user2Updated = userRepository.findByEmail("user2@gmail.com").orElseThrow();

        assertThat(userUpdated.getBalance()).isEqualByComparingTo("80.00");
        assertThat(user2Updated.getBalance()).isEqualByComparingTo("70.00");

        Transaction transaction = transactionRepository.findAll().get(0);
        assertThat(transaction.getSender().getEmail()).isEqualTo("user@gmail.com");
        assertThat(transaction.getReceiver().getEmail()).isEqualTo("user2@gmail.com");
        assertThat(transaction.getAmount()).isEqualByComparingTo("20.00");
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testTransactionFailsWhenContactNotFound() throws Exception {
        mockMvc.perform(post("/transfert")
                        .param("receiverEmail", "invalidUser@gmail.com")
                        .param("amount", "15.00"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfert"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testTransactionFailsWhenInsufficientBalance() throws Exception {
        mockMvc.perform(post("/transfert")
                        .param("receiverEmail", "user2@gmail.com")
                        .param("amount", "500.00")
                        .param("description", "Insufficient Balance."))
                .andExpect(status().isOk())
                .andExpect(view().name("transfert"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Votre solde est insuffisant."));

        // Vérifier que rien n'a changé
        User userUpdated = userRepository.findByEmail("user@gmail.com").orElseThrow();
        User user2Updated = userRepository.findByEmail("user2@gmail.com").orElseThrow();

        assertThat(userUpdated.getBalance()).isEqualByComparingTo("100.00");
        assertThat(user2Updated.getBalance()).isEqualByComparingTo("50.00");

        assertThat(transactionRepository.findAll()).isEmpty();
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testTransfertPageShowsTransactions() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setSender(user);
        transaction.setReceiver(user2);
        transaction.setAmount(BigDecimal.valueOf(30));
        transaction.setDescription("Transaction.");
        transactionRepository.save(transaction);

        mockMvc.perform(get("/transfert"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfert"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attribute("transactions",
                        org.hamcrest.Matchers.hasItem(
                                org.hamcrest.Matchers.hasProperty("description", org.hamcrest.Matchers.equalTo("Transaction."))
                        )));
    }

}
