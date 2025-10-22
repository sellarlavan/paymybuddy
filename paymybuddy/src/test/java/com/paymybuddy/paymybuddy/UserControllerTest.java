package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
        userConnectionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testRegisterCreatesUser() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "user")
                        .param("email", "user@gmail.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        User savedUser = userRepository.findByEmail("user@gmail.com").orElseThrow();
        assertThat(savedUser.getUsername()).isEqualTo("user");
        assertThat(savedUser.getPassword()).isNotEqualTo("password");
    }

    @Test
    void testRegisterWithExistingEmailFails() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "user")
                        .param("email", "user@gmail.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        userRepository.flush();

        mockMvc.perform(post("/register")
                        .param("username", "user2")
                        .param("email", "user@gmail.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "L'email est déjà utilisé."));
    }

    @Test
    void testRegisterWithExistingUsernameFails() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "user")
                        .param("email", "user@gmail.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        userRepository.flush();

        mockMvc.perform(post("/register")
                        .param("username", "user")
                        .param("email", "user2@gmail.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Le nom d'utilisateur est déjà utilisé."));
    }
}
