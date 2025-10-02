package main.java.com.app.repository.impl;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.models.AccountExterne;
import main.java.com.app.repository.interfaces.AccountExternRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountExternImpl implements AccountExternRepository {
    public Optional<AccountExterne> findAccountExternbyId(String clientId){
        String sql = "select * from accounts_externe where account_number = ?";
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,clientId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return Optional.of(mapResultSetToAccountExtern(rs));
            }
        }catch(SQLException e){
            System.err.println("Erreur recherche compte par numéro : " + e.getMessage());
        }
        return Optional.empty();

    }
    public AccountExterne update(AccountExterne accountExterne) {
        String sql = "UPDATE accounts_externe SET balance = ?, bank_name = ?, holder_name = ? WHERE account_number = ?";
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, accountExterne.getBalance());
            stmt.setString(2, accountExterne.getBankName());
            stmt.setString(3, accountExterne.getHolderName());
            stmt.setString(4, accountExterne.getAccountNumber());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows > 0) {
                return accountExterne;
            }
        } catch(SQLException e) {
            System.err.println("Erreur mise à jour AccountExterne : " + e.getMessage());
        }
        return null;
    }
    public AccountExterne mapResultSetToAccountExtern(ResultSet rs) throws SQLException{
        AccountExterne accountExtern = new AccountExterne();
            accountExtern.setId(rs.getLong("id"));
            accountExtern.setAccountNumber(rs.getString("account_number"));
            accountExtern.setBalance(rs.getBigDecimal("balance"));
            accountExtern.setBankName(rs.getString("bank_name"));
            accountExtern.setHolderName(rs.getString("holder_name"));
           accountExtern.setCreatedAt(rs.getTimestamp("created_at").toInstant());

            return accountExtern;
    }
}
