package main.java.com.app.models;

import java.math.BigDecimal;
import java.time.Instant;

public class AccountExterne {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String bankName;
    private String holderName;
    private Instant createdAt;
    public AccountExterne(){}
    public AccountExterne(String accountNumber,BigDecimal balance ,String  bankName, String holderName) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.bankName = bankName;
        this.holderName = holderName;
        this.createdAt = Instant.now();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
