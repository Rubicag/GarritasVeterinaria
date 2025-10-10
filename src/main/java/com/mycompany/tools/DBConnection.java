package com.mycompany.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Build URL from parameters and return a Connection
    public static Connection getConnection(String host, String port, String database, String user, String password) throws SQLException {
        String url = String.format("jdbc:mysql://http://localhost/phpmyadmin/index.php?route=/database/structure&server=1&db=garritas_veterinaria", host, port, database);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MySQL", e);
        }
    }
}
