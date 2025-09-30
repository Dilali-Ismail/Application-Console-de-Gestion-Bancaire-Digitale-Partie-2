package main.java.com.app.repository.impl;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.models.Transaction;
import main.java.com.app.models.enums.TransactionType;
import main.java.com.app.repository.interfaces.TransactionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    public Transaction save(Transaction transaction){

        String Sql = "Insert into transactions (account_id, transaction_type, amount, status ) values (?, ?, ?, ?)";
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(Sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, transaction.getAccountId());
            stmt.setString(2, transaction.getTransactionType().name());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setString(4, transaction.getStatus());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    transaction.setId(rs.getLong(1));
                    return transaction;
                }
            }
        }catch(Exception e){
            System.err.println("Erreur sauvegarde transaction : " + e.getMessage());
        }
        return null;
    }
    public List<Transaction> findByAccountId(Long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "select * from transaction where accoun_id =?";
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement smt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            smt.setLong(1, accountId);
            ResultSet rs = smt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }

        } catch (Exception e) {
            System.err.println("Erreur recherche transactions par accountId : " + e.getMessage());
    }
        return transactions;
    }

    public Transaction mapResultSetToTransaction (ResultSet rs) {
        Transaction transaction = null;
        try{
            transaction = new Transaction();
            transaction.setId(rs.getLong("id"));
            transaction.setAccountId(rs.getLong("account_id"));
            transaction.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
            transaction.setAmount(rs.getBigDecimal("amount"));
            transaction.setStatus(rs.getString("status"));
            transaction.setCreatedAt(rs.getTimestamp("created_at").toInstant());
            return transaction;

        }catch(Exception e){
            System.err.println("Erreur mapping transaction : " + e.getMessage());
        }
        return transaction;
    }
}
