package main.java.com.app.controller;

import main.java.com.app.service.TransactionService;

import java.math.BigDecimal;

public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    public void deposit(String accountNumber, BigDecimal amount) {
        // Validation simple
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de compte requis");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }
        transactionService.deposit(accountNumber.trim(), amount);

    }
    public void withdraw(String accountNumber ,BigDecimal amount){
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de compte requis");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }
        transactionService.withdraw(accountNumber.trim(), amount);
    }

    public void transfer(String fromAccountNumber , String toAccountNumber , BigDecimal amount){
        if (fromAccountNumber == null || fromAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de compte source requis");
        }
        if (toAccountNumber == null || toAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de compte destination requis");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }
        transactionService.transfer(fromAccountNumber.trim(), toAccountNumber.trim(), amount);
    }
    public BigDecimal getAccountBalance(String accountNumber) {
        return transactionService.getAccountBalance(accountNumber);
    }
}
