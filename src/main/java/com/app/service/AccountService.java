package main.java.com.app.service;

import main.java.com.app.models.Account;
import main.java.com.app.models.Client;
import main.java.com.app.models.enums.AccountType;
import main.java.com.app.repository.impl.AccountRepositoryImpl;
import main.java.com.app.repository.interfaces.AccountRepository;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

public class AccountService {

    private AccountRepository accountRepository;
    private int accountCounter = 1;

    public AccountService() {
        this.accountRepository = new AccountRepositoryImpl();
    }

    public Account createAccount(Client client, AccountType accountType, BigDecimal initialBalance) {
        validateAccountCreation(client, accountType, initialBalance);

        String accountNumber = generateAccountNumber();

        Account account = new Account(client.getId(), accountType, initialBalance);

        account.setAccountNumber(accountNumber);

        configureAccountByType(account, accountType);

        Account savedAccount = accountRepository.save(account);
        if (savedAccount == null) {
            throw new RuntimeException("Erreur lors de la création du compte");
        }

        return savedAccount;

    }

    public List<Account> getClientAccounts(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé : " + accountNumber));
    }

    private void validateAccountCreation(Client client, AccountType accountType, BigDecimal initialBalance) {
        if (client == null) {
            throw new IllegalArgumentException("Client requis");
        }

        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le solde initial ne peut pas être négatif");
        }
        switch (accountType) {
            case SAVINGS:
                if (initialBalance.compareTo(new BigDecimal("100")) < 0) {
                    throw new IllegalArgumentException("Solde minimum de 100 MAD requis pour un compte épargne");
                }
                break;
            case CHECKING:
                // Compte courant peut avoir solde = 0
                break;
            case CREDIT:
                // Logique spécifique crédit à implémenter
                break;
        }
    }

    private void configureAccountByType(Account account, AccountType accountType) {
        switch (accountType) {
            case CHECKING:
                account.setOverdraftLimit(new BigDecimal("500"));
                account.setInterestRate(BigDecimal.ZERO);
                break;
            case SAVINGS:
                account.setOverdraftLimit(BigDecimal.ZERO);
                account.setInterestRate(new BigDecimal("0.02"));
            case CREDIT:
                account.setOverdraftLimit(BigDecimal.ZERO);
                account.setInterestRate(new BigDecimal("0.05"));
                break;
        }


    }

    private String generateAccountNumber() {
        String year = String.valueOf(Year.now().getValue());
        String sequence = String.format("%04d", accountCounter++);
        return "BK-" + year + "-" + sequence;
    }

    public boolean clientHasAccountType(Long clientId, AccountType accountType) {
        List<Account> clientAccounts = getClientAccounts(clientId);
        return clientAccounts.stream()
                .anyMatch(account -> account.getAccountType() == accountType && account.getStatus().equals("ACTIVE"));
    }
}