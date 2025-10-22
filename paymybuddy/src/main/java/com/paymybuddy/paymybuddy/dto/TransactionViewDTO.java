package com.paymybuddy.paymybuddy.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionViewDTO {
    private String contactUsername;
    private String description;
    private BigDecimal amount;
    private boolean sent;

    public TransactionViewDTO(String otherParty, String description, BigDecimal amount, boolean sent) {
        this.contactUsername = otherParty;
        this.description = description;
        this.amount = amount;
        this.sent = sent;
    }

}