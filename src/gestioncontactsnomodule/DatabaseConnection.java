package gestioncontactsnomodule;

import java.sql.Connection;
import java.sql.DriverManager; //permet de se connecter à la base
import java.sql.SQLException;
import java.sql.Statement; //permet d’exécuter une requête SQL

public class DatabaseConnection {
    // Path to the SQLite database in the lib directory of the project
	private static final String DB_URL = "jdbc:sqlite:C:/Users/jihen/eclipse-workspace/GestionContactsNoModule/base_de_donnée/contact.db";


    // s'exécute automatiquement une seule fois quand la classe est chargée.
    static {
        try {
            
            Class.forName("org.sqlite.JDBC"); // Explicitly load the SQLite JDBC driver
            System.out.println("SQLite JDBC driver loaded successfully");
            initializeDatabase(); //Crée la table contacts si elle n’existe pas encore.
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found. Ensure sqlite-jdbc-3.46.1.3.jar is in the module path and 'requires org.sqlite.jdbc' is in module-info.java.", e);
        }
    }

    // Méthode pour se connecter à la base
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL); //tarbet lprog bel base
        conn.createStatement().execute("PRAGMA foreign_keys = ON");// Active les clés étrangères
        return conn;
    }

    // Crée la table contacts si elle n’existe pas déjà.
    private static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS contacts (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nom TEXT NOT NULL," +
                     "prenom TEXT," +
                     "telephone TEXT," +
                     "email TEXT)";
        try (Connection conn = DriverManager.getConnection(DB_URL);  //tarbet lprog bel base
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database initialized successfully: " + DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database: " + e.getMessage(), e);
        }
    }

    // Test method to verify database connection
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Successfully connected to " + DB_URL);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }
}