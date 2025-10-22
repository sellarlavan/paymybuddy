package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void testLoginWithValidCredentials() throws Exception {
        userService.register(new UserRegistrationDTO(
                "user",
                "user@gmail.com",
                "password"
        ));

        mockMvc.perform(formLogin("/login")
                        .user("user@gmail.com")
                        .password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfert"));
    }

    @Test
    void testLoginWithInvalidPasswordFails() throws Exception {

        userService.register(new UserRegistrationDTO(
                "invalidUser",
                "invalidUser@gmail.com",
                "password"
        ));

        mockMvc.perform(formLogin("/login")
                        .user("invalidUser@gmail.com")
                        .password("invalidPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}
