package main.java.com.app.repository.interfaces;

import main.java.com.app.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);
    List<Account> findByClientId(Long clientId);
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean accountNumberExists(String accountNumber);

}

