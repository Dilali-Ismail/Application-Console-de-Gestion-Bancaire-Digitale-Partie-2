package main.java.com.app.repository.impl;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.models.Client;
import main.java.com.app.repository.interfaces.ClientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    @Override
    public Client save(Client client) {
        String sql = "INSERT INTO clients (FullName, email, phone, address, monthly_income) VALUES (?, ?, ?, ?, ?)";

        try  {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getAddress());
            stmt.setBigDecimal(5, client.getMonthlyIncome());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    client.setId(rs.getLong(1));
                    return client;
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur sauvegarde client : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Optional<Client> findById(Long id) {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Client client = new Client(
                        rs.getString("FullName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBigDecimal("monthly_income")
                );
                client.setId(rs.getLong("id"));
                client.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                return Optional.of(client);
            }

        } catch (Exception e) {
            System.err.println("Erreur recherche client par ID : " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getString("FullName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBigDecimal("monthly_income")
                );
                client.setId(rs.getLong("id"));
                client.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                clients.add(client);
            }

        } catch (Exception e) {
            System.err.println("Erreur récupération clients : " + e.getMessage());
        }

        return clients;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM clients WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Erreur suppression client : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM clients WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.err.println("Erreur vérification email : " + e.getMessage());
        }

        return false;
    }
}