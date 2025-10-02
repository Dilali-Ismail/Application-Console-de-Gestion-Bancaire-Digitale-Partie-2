package main.java.com.app.repository.interfaces;

import main.java.com.app.models.Account;
import main.java.com.app.models.BankFee;

public interface BankFeeRepository {
    BankFee save(BankFee bankFee);
}
