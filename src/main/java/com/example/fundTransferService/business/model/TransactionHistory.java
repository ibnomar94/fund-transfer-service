package com.example.fundTransferService.business.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transaction_history")
@Getter
@Setter
public class TransactionHistory {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_id")
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "credited_id")
    private Account creditedAccount;

    @ManyToOne
    @JoinColumn(name = "debited_id")
    private Account debitedAccount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "exchange_fee")
    private BigDecimal exchangeFee;
}
