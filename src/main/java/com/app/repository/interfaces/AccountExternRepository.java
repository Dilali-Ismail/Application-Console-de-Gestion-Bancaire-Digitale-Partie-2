package main.java.com.app.repository.interfaces;

import main.java.com.app.models.AccountExterne;

import java.util.Optional;

public interface AccountExternRepository {
    Optional<AccountExterne> findAccountExternbyId(String clientId);
    AccountExterne update(AccountExterne accountExterne);

}
