package com.paymybuddy.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {

    @NotBlank
    @Email
    private String receiverEmail;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    private String description;
}
