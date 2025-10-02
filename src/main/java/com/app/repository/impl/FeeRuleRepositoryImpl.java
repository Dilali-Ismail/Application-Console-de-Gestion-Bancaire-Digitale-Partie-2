package main.java.com.app.repository.impl;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.models.FeeRule;
import main.java.com.app.models.enums.FeeMode;
import main.java.com.app.repository.interfaces.FeeRuleRepository;

import java.lang.classfile.attribute.StackMapTableAttribute;
import java.nio.file.WatchKey;
import java.sql.*;
import java.util.Optional;

public class FeeRuleRepositoryImpl implements FeeRuleRepository {

    public FeeRule save(FeeRule feeRule){
        String sql = "INSERT INTO fee_rules(operation_type,fee_mode,fee_value,currency,is_active,created_at) VALUES(?,?,?,?,?,?)";
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, feeRule.getOperationType());
            stmt.setString(2,feeRule.getFeeMode().name());
            stmt.setBigDecimal(3, feeRule.getFeeValue());
            stmt.setString(4, feeRule.getCurrency());
            stmt.setBoolean(5, feeRule.getActive());
            stmt.setObject(6, feeRule.getCreatedAt());
            int affectedRows = stmt.executeUpdate();
            if(affectedRows > 0){
                var rs = stmt.getGeneratedKeys();
                if(rs.next()){
                    feeRule.setId(rs.getLong(1));
                    return feeRule;
                }
            }
            return null;

        }catch(Exception e){
            System.err.println("Erreur sauvegarde FeeRule : " + e.getMessage());
            return null;
        }
    }
    public Optional<FeeRule> findActiveByOperationType(String operationType){
        String Sql = "Select * from  fee_rules where operation_type =? and is_active = true";
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt =  connection.prepareStatement(Sql,Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,operationType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToFeeRule(rs));
            }
        }catch(Exception e){
            System.err.println("Erreur recherche FeeRule par operationType : " + e.getMessage());

        }
        return Optional.empty();
    }
    public FeeRule mapResultSetToFeeRule (ResultSet rs){
        FeeRule feeRule = new FeeRule();
        try {
            feeRule.setId(rs.getLong("id"));
            feeRule.setOperationType(rs.getString("operation_type"));
            feeRule.setFeeMode(FeeMode.valueOf(rs.getString("fee_mode")));
            feeRule.setFeeValue(rs.getBigDecimal("fee_value"));
            feeRule.setCurrency(rs.getString("currency"));
            feeRule.setActive(rs.getBoolean("is_active"));
            feeRule.setCreatedAt(rs.getTimestamp("created_at").toInstant());
           return feeRule;
        }catch (SQLException e){
            System.out.println("Erreur mapping FeeRule : " + e.getMessage());
        }

        return feeRule;

    }

}
