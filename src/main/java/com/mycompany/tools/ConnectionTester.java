package com.mycompany.tools;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTester {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";
        String port = (args.length > 1) ? args[1] : "3306";
        String db = (args.length > 2) ? args[2] : "garritas_veterinaria";
        String user = (args.length > 3) ? args[3] : "root";
        String pass = (args.length > 4) ? args[4] : "";

        System.out.println("Probando conexión a: " + host + ":" + port + "/" + db + " con usuario " + user);
        try (Connection c = DBConnection.getConnection(host, port, db, user, pass)) {
            if (c != null && !c.isClosed()) {
                System.out.println("Conexión OK. AutoCommit=" + c.getAutoCommit());
            } else {
                System.out.println("Conexión devuelta nula o cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Fallo al conectar: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
