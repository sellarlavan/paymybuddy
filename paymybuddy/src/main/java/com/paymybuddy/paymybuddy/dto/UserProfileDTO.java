package com.paymybuddy.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserProfileDTO {
    private String username;
    private String email;
    private String password;
}
