/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author elias
 */
public class DBConnectionProviderHealth {
    
    String dbPath = "C:\\Users\\domat\\OneDrive\\semester 7\\Programming\\projects\\GitHealthApp\\src\\database\\";

    String dbName = "databaseHealth.db";

    String engine = "jdbc:sqlite";

    String connectionString = engine + ":" + dbPath + dbName;

    public Connection getConnection() {

        try {
            Connection dbConnection = DriverManager.getConnection(connectionString);
            return dbConnection;

        } catch (SQLException ex) {
            System.out.println("An Error Has Happened: " + ex.getMessage());

        }
        return null;
    }
}
