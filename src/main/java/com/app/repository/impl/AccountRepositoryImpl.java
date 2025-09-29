package main.java.com.app.repository.impl;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.models.Account;
import main.java.com.app.models.enums.AccountType;
import main.java.com.app.repository.interfaces.AccountRepository;

import java.lang.invoke.StringConcatFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public Account save(Account account) {
        String sql = "INSERT INTO accounts (account_number, client_id, account_type, balance, currency,overdraft_limit, interest_rate, status, opened_date) Values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getAccountNumber());
            stmt.setLong(2, account.getClientId());
            stmt.setString(3, account.getAccountType().name());
            stmt.setBigDecimal(4, account.getBalance());
            stmt.setString(5, account.getCurrency());
            stmt.setBigDecimal(6, account.getOverdraftLimit());
            stmt.setBigDecimal(7, account.getInterestRate());
            stmt.setString(8, account.getStatus());
            stmt.setDate(9, Date.valueOf(account.getOpenedDate()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    account.setId(rs.getLong(1));
                    return account;
                }
            }
        }catch (Exception e) {
            System.err.println("Erreur sauvegarde compte : " + e.getMessage());
        }

        return null;
    }

    public List<Account> findByClientId(Long clientId){
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE client_id = ?";
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

        }catch(Exception e){
            System.err.println("Erreur recherche comptes par clientId : " + e.getMessage());
        }
        return accounts;
    }

   public Optional<Account> findByAccountNumber(String accountNumber){
        String sql ="Select * from accounts where account_number = ?";
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return Optional.of(mapResultSetToAccount(rs));
            }
        }catch (Exception  e){
            System.err.println("Erreur recherche compte par numéro : " + e.getMessage());
        }
       return Optional.empty();
   }

    public boolean accountNumberExists(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.err.println("Erreur vérification numéro compte : " + e.getMessage());
        }

        return false;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setClientId(rs.getLong("client_id"));
        account.setAccountType(AccountType.valueOf(rs.getString("account_type")));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setCurrency(rs.getString("currency"));
        account.setOverdraftLimit(rs.getBigDecimal("overdraft_limit"));
        account.setInterestRate(rs.getBigDecimal("interest_rate"));
        account.setStatus(rs.getString("status"));
        account.setOpenedDate(rs.getDate("opened_date").toLocalDate());

        Date closedDate = rs.getDate("closed_date");
        if (closedDate != null) {
            account.setClosedDate(closedDate.toLocalDate());
        }

        account.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        return account;
    }

}