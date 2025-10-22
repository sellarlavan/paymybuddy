package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.UserConnection;
import com.paymybuddy.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserConnectionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    private User user;
    private User user2;

    @BeforeEach
    void setup() {
        userConnectionRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        userRepository.save(user);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password");
        userRepository.save(user2);
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testAddContact() throws Exception {
        mockMvc.perform(post("/connection/add")
                        .param("contactEmail", "user2@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connection"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attribute("success", "user2@gmail.com a été ajouté en relation."));

        boolean exists = userConnectionRepository.existsByUserAndContact(user, user2);
        assertThat(exists).isTrue();
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testAddNonExistingContactFails() throws Exception {
        mockMvc.perform(post("/connection/add")
                        .param("contactEmail", "userInvalid@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connection"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Contact introuvable."));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testAddAlreadyExistingContactFails() throws Exception {
        userConnectionRepository.save(new UserConnection(user, user2));

        mockMvc.perform(post("/connection/add")
                        .param("contactEmail", "user2@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connection"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Ce contact est déjà dans vos contacts."));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testAddSelfAsContactFails() throws Exception {
        mockMvc.perform(post("/connection/add")
                        .param("contactEmail", "user@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connection"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Impossible de s'ajouter soi-même comme contact."));
    }
}
