package main.java.com.app.controller;

import main.java.com.app.models.Account;
import main.java.com.app.models.Client;
import main.java.com.app.models.enums.AccountType;
import main.java.com.app.repository.interfaces.AccountRepository;
import main.java.com.app.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

public class AccountController {
    private AccountService accountService;
    private AuthController authController;

    public AccountController(AccountService accountService, AuthController authController) {
        this.accountService = accountService;
        this.authController = authController;
    }

    public Account creerAccount(Client client , AccountType accountType , BigDecimal balance){
        authController.ensureTellerAccess();
        if (client == null) {
            throw new IllegalArgumentException("Client requis");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Type de compte requis");
        }

        return accountService.createAccount(client , accountType , balance);
    }

    public List<Account> getClientsAccounts (Long crientId){
         authController.ensureTellerAccess();
         return accountService.getClientAccounts(crientId);
    }

    public Account getClientbyNumber(String accountNumber){
        authController.ensureTellerAccess();
        return accountService.getAccountByNumber(accountNumber);
    }

    public boolean clientHasAccountType(Long clientId, AccountType accountType) {
        authController.ensureTellerAccess();
        return accountService.clientHasAccountType(clientId, accountType);
    }

}
