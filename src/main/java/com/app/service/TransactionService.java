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
       if(account.getAccountType().equals())
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

    public Transaction withdraw(String accountnumber ,BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountnumber)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé : " + accountnumber));
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Erreur mise à jour du solde");
        }
        Transaction savedTransaction = null;
        if (account.getBalance().compareTo(amount) > 0) {
            BigDecimal newbalaance = account.getBalance().subtract(amount);
            account.setBalance(newbalaance);
            Account updatedAccount = accountRepository.update(account);
            if (updatedAccount == null) {
                throw new RuntimeException("Erreur mise à jour du solde");
            }
            Transaction transaction = new Transaction(account.getId(), TransactionType.WITHDRAWAL, amount);
            savedTransaction = transactionRepository.save(transaction);
            if (savedTransaction == null) {
                throw new RuntimeException("Erreur création de la transaction");
            }

           }
        return savedTransaction;
    }

    public void transfer(String accounnumberOut , String accountnumberIn , BigDecimal amount){
        Account accountOut = accountRepository.findByAccountNumber(accounnumberOut)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé : " + accounnumberOut));
        Account accountIn = accountRepository.findByAccountNumber(accountnumberIn)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé : " + accountnumberIn));
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Erreur mise à jour du solde");
        }
        if(accountOut.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Solde insuffisant pour le transfert");
        }

        accountOut.setBalance(accountOut.getBalance().subtract(amount));
        accountIn.setBalance(accountIn.getBalance().subtract(amount));
        //update
        Account updatedAccountOut = accountRepository.update(accountOut);
        Account updatedAccountIn = accountRepository.update(accountIn);

        if (updatedAccountOut == null || updatedAccountIn == null) {
            throw new RuntimeException("Erreur mise à jour du solde");
        }

        Transaction transactionOut = new Transaction(accountOut.getId(), TransactionType.TRANSFER_OUT, amount);
        Transaction transactionIn = new Transaction(accountIn.getId(), TransactionType.TRANSFER_IN, amount);

        transactionRepository.save(transactionOut);
        transactionRepository.save(transactionIn);
    }

    public BigDecimal getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé : " + accountNumber));
        return account.getBalance();
    }
}
