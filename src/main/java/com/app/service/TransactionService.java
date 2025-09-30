package main.java.com.app.service;

import main.java.com.app.models.Account;
import main.java.com.app.models.Transaction;
import main.java.com.app.models.enums.TransactionType;
import main.java.com.app.repository.impl.AccountRepositoryImpl;
import main.java.com.app.repository.impl.TransactionRepositoryImpl;
import main.java.com.app.repository.interfaces.AccountRepository;
import main.java.com.app.repository.interfaces.TransactionRepository;

import java.math.BigDecimal;

public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionService() {

        this.transactionRepository = new TransactionRepositoryImpl();
        this.accountRepository = new AccountRepositoryImpl();
    }
    public Transaction deposit(String accountNumber, BigDecimal amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé : " + accountNumber));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        System.out.println(account.getBalance());
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        System.out.println(newBalance);

        Account updatedAccount = accountRepository.update(account);
        if (updatedAccount == null) {
            throw new RuntimeException("Erreur mise à jour du solde");
        }
        Transaction transaction = new Transaction(account.getId(), TransactionType.DEPOSIT, amount);
        Transaction savedTransaction = transactionRepository.save(transaction);
        if (savedTransaction == null) {
            throw new RuntimeException("Erreur création de la transaction");
        }
        return savedTransaction;
    }
    public BigDecimal getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé : " + accountNumber));
        return account.getBalance();
    }
}
