package com.paymybuddy.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "fk_transactions_sender"))
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", foreignKey = @ForeignKey(name = "fk_transactions_receiver"))
    private User receiver;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Size(max = 255)
    private String description;
}
