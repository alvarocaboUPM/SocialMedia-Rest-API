package sos.rest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class TestDbConnection {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Dotenv dotenv =Dotenv.load();
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Open a connection
            System.out.println("Connecting to database...");
            String url = "jdbc:mysql://" + dotenv.get("DB_HOST")+":"+dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME"); // URL para la conexión
;
            conn = DriverManager.getConnection(url, dotenv.get("DB_USER"), dotenv.get("DB_PW"));

            // Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users");

            // Process the results
            while (rs.next()) {
                System.out.println(rs.getLong("user_id") + " " + rs.getString("name") + " " + rs.getString("email"));
            }
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}