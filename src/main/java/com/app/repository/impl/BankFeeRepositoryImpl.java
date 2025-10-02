package main.java.com.app.repository.impl;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.models.BankFee;
import main.java.com.app.repository.interfaces.BankFeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankFeeRepositoryImpl implements BankFeeRepository {

    public BankFee save(BankFee bankFee){
        String sql = "INSERT INTO bank_fees(source_type,source_id,amount,fee_type,currency) values(?,?,?,?,?)";
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, bankFee.getSourceType());
            stmt.setLong(2, bankFee.getSourceId());
            stmt.setBigDecimal(3,bankFee.getAmount());
            stmt.setString(4, bankFee.getFeeType());
            stmt.setString(5, bankFee.getCurrency());
            int affectedRows = stmt.executeUpdate();
            if(affectedRows > 0){
                var rs = stmt.getGeneratedKeys();
                if(rs.next()){
                    bankFee.setId(rs.getLong(1));
                    return bankFee;
                }
            }
        }catch (SQLException e){
            System.err.println("Erreur sauvegarde BankFee : " + e.getMessage());
            return null;
        }
        return bankFee;


    }
}
