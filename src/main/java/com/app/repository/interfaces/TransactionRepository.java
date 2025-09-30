package main.java.com.app.repository.interfaces;

import main.java.com.app.models.Transaction;

import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountId(Long accountId);
}

