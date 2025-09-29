package main.java.com.app.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    // keep one optional test connection, but DAOs should use getConnection() to obtain fresh connections
    private Connection connection;

    // store credentials to return new connections per call
    private String url;
    private String username;
    private String password;
    private String driver;

    private DatabaseConnection() {
        connectToDatabase();
        initializeDatabase();
    }

    // Méthode singleton
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Connexion à la base (charge les propriétés et initialise une connexion de test)
    private void connectToDatabase() {
        try {
            // Charger le fichier properties
            InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
            if (input == null) {
                throw new RuntimeException("application.properties not found in classpath");
            }
            Properties props = new Properties();
            props.load(input);
            input.close();

            // Récupérer la configuration
            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver");

            // Charger le driver et établir une connexion de test
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, username, password);

            System.out.println(" ✅ Connexion BD réussie : " + url);

        } catch (Exception e) {
            System.err.println(" ❌ Erreur connexion BD : " + e.getMessage());
            throw new RuntimeException("Échec connexion base de données", e);
        }
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection()) {
            InputStream input = getClass().getClassLoader().getResourceAsStream("schema.sql");
            if (input == null) {
                System.err.println("schema.sql not found in classpath - skipping initialization");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            try (Statement stmt = conn.createStatement()) {
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                        continue;
                    }
                    sqlBuilder.append(line).append('\n');

                    // Exécuter quand on rencontre un point-virgule
                    if (line.trim().endsWith(";")) {
                        String sql = sqlBuilder.toString().trim();
                        if (!sql.isEmpty()) {
                            try {
                                stmt.execute(sql);
                            } catch (Exception ex) {
                                // log and continue: schema may contain inserts that conflict
                                System.err.println("Erreur execution SQL: " + ex.getMessage());
                            }
                        }
                        sqlBuilder.setLength(0); // Reset le builder
                    }
                }
            }

            reader.close();
            System.out.println(" ✅ Schéma de base de données initialisé avec succès");

        } catch (Exception e) {
            System.err.println(" ❌ Erreur initialisation schéma BD : " + e.getMessage());
        }
    }

    // Return a new Connection for each caller (DAOs should close the connection when done)
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'obtenir une connexion à la base : " + e.getMessage(), e);
        }
    }

    // Fermer la connexion de test si elle existe
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Connexion BD fermée");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur fermeture BD : " + e.getMessage());
        }
    }
}