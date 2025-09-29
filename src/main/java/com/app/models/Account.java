package main.java.com.app.models;

import main.java.com.app.models.enums.AccountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Account {

    private Long id;
    private String accountNumber;
    private Long clientId;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private BigDecimal overdraftLimit;
    private BigDecimal interestRate;
    private String status;
    private LocalDate openedDate;
    private LocalDate closedDate;
    private Instant createdAt;
    public Account(){

    }
    public Account(Long clientId, AccountType accountType, BigDecimal balance) {
        this.clientId = clientId;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = "MAD";
        this.overdraftLimit = BigDecimal.ZERO;
        this.interestRate = BigDecimal.ZERO;
        this.status = "ACTIVE";
        this.openedDate = LocalDate.now();
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(BigDecimal overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getOpenedDate() { return openedDate; }
    public void setOpenedDate(LocalDate openedDate) { this.openedDate = openedDate; }

    public LocalDate getClosedDate() { return closedDate; }
    public void setClosedDate(LocalDate closedDate) { this.closedDate = closedDate; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return accountNumber + " - " + accountType + " - " + balance + " " + currency;
    }


}
