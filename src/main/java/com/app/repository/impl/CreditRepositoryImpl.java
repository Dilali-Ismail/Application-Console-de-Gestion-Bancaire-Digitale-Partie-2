package main.java.com.app.repository.impl;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.models.Credit;
import main.java.com.app.repository.interfaces.CreditRepository;

import javax.security.auth.login.CredentialNotFoundException;
import java.sql.*;
import java.util.Date;
import java.time.LocalDate;

public class CreditRepositoryImpl implements CreditRepository {
    public Credit save(Credit credit) {
        String sql = "INSERT INTO credits(amount, duration, monthly_payment, status, remaining_amount, client_id, request_date, client_income) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setBigDecimal(1, credit.getAmount());
            stmt.setInt(2, credit.getDuration());
            stmt.setBigDecimal(3, credit.getMonthlyPayment());
            stmt.setString(4, credit.getStatus().name());
            stmt.setBigDecimal(5, credit.getRemainingAmount());
            stmt.setLong(6, credit.getClient());
            stmt.setDate(7, java.sql.Date.valueOf(credit.getRequestDate()));
            stmt.setBigDecimal(8, credit.getClientIncome());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    Credit savedCredit = new Credit(
                            rs.getLong(1),
                            credit.getAmount(),
                            credit.getDuration(),
                            credit.getMonthlyPayment(),
                            credit.getStatus(),
                            credit.getRemainingAmount(),
                            credit.getClient(),
                            credit.getRequestDate(),
                            credit.getClientIncome()
                    );
                    return savedCredit;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde crédit: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }



}
