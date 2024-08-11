package com.example.fundTransferService.business.model;

import java.math.BigDecimal;

import com.example.fundTransferService.business.domain.Currency;
import com.example.fundTransferService.exception.InsufficientFundsException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "ACCOUNT")
@Getter
@Setter
public class Account {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "iban")
    private String iban;

    // TODO investigate to see if it would be better to use varchar in the database for more accuracy
    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "account_holder_id")
    private AccountHolder accountHolder;


    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        this.balance = this.balance.subtract(amount);
    }


}
