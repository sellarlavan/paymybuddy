package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.model.User;
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
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        user = new User();
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setBalance(BigDecimal.valueOf(100));
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testProfilePageShowsUserInfo() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user",
                        hasProperty("username", is("user"))))
                .andExpect(model().attribute("user",
                        hasProperty("email", is("user@gmail.com"))));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testUpdateProfileSuccess() throws Exception {
        mockMvc.perform(post("/profile")
                        .param("username", "userUpdated")
                        .param("email", "userUpdated@gmail.com")
                        .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile?success"));

        User userUpdated = userRepository.findByEmail("userUpdated@gmail.com").orElseThrow();
        assertThat(userUpdated.getUsername()).isEqualTo("userUpdated");
        assertThat(passwordEncoder.matches("newpassword", userUpdated.getPassword())).isTrue();
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testUpdateProfileFailsIfEmailAlreadyTaken() throws Exception {

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user2);

        mockMvc.perform(post("/profile")
                        .param("username", "userUpdated")
                        .param("email", "user2@gmail.com")
                        .param("password", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "L'email est déjà utilisé."));

        User originUser = userRepository.findByEmail("user@gmail.com").orElseThrow();
        assertThat(originUser.getUsername()).isEqualTo("user");
        assertThat(passwordEncoder.matches("password", originUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testUpdateProfileFailsIfUsernameAlreadyTaken() throws Exception {

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user2);

        mockMvc.perform(post("/profile")
                        .param("username", "user2") // username déjà pris
                        .param("email", "userUpdated@gmail.com")
                        .param("password", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Le nom d'utilisateur est déjà utilisé."));

        User originUser = userRepository.findByEmail("user@gmail.com").orElseThrow();
        assertThat(originUser.getUsername()).isEqualTo("user");
        assertThat(passwordEncoder.matches("password", originUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void testUpdateProfileUsernameAndEmailOnlyDoesNotChangePassword() throws Exception {
        String oldPasswordHash = user.getPassword();

        mockMvc.perform(post("/profile")
                        .param("username", "userUpdated")
                        .param("email", "userUpdated@gmail.com")
                        .param("password", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile?success"));

        User userUpdated = userRepository.findByEmail("userUpdated@gmail.com").orElseThrow();

        assertThat(userUpdated.getUsername()).isEqualTo("userUpdated");
        assertThat(userUpdated.getEmail()).isEqualTo("userUpdated@gmail.com");
        assertThat(userUpdated.getPassword()).isEqualTo(oldPasswordHash);
    }

}
