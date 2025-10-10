package com.mycompany.tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.stream.Collectors;

/**
 * DbImportTool - utilidad pequeña para:
 *  - realizar un backup sencillo (SELECT * -> INSERT statements) a backup_garritas.sql
 *  - ejecutar seed_import.sql en la base de datos remota
 *
 * Uso (desde el proyecto):
 * mvn -q compile exec:java -Dexec.mainClass="com.mycompany.tools.DbImportTool" -Dexec.args="host port db user password backup(true|false)"
 *
 */
public class DbImportTool {

    public static void main(String[] args) throws Exception {
        // Accept either 5 or 6 arguments. If 5 arguments are provided we treat the password as empty
        // and the 5th parameter is the backup flag. If 6 are provided, the 5th is password and 6th is backup.
        if (args.length < 5) {
            System.err.println("Uso: DbImportTool <host> <port> <database> <user> <password|(omit for empty)> <hacer_backup:true|false>");
            System.err.println("O: DbImportTool <host> <port> <database> <user> <hacer_backup:true|false>  (si quieres password vacío)");
            System.exit(2);
        }
        String host = args[0];
        String port = args[1];
        String db = args[2];
        String user = args[3];
        String password;
        boolean doBackup;
        if (args.length == 5) {
            // password omitted -> empty password; 5th arg is backup flag
            password = "";
            doBackup = Boolean.parseBoolean(args[4]);
        } else {
            password = args[4];
            doBackup = Boolean.parseBoolean(args[5]);
        }

    System.out.println("Conectando a: " + host + ":" + port + "/" + db + " como usuario " + user + " (backup=" + doBackup + ")");

    try (Connection conn = DBConnection.getConnection(host, port, db, user, password)) {
            conn.setAutoCommit(false);
            if (doBackup) {
                System.out.println("Realizando backup de tablas usuarios y mascotas a backup_garritas.sql ...");
                Path out = Path.of("backup_garritas.sql");
                try (BufferedWriter w = Files.newBufferedWriter(out)) {
                    dumpTableAsInserts(conn, w, "usuarios");
                    dumpTableAsInserts(conn, w, "mascotas");
                }
                System.out.println("Backup completado: " + out.toAbsolutePath());
            }

            // Ejecutar seed_import.sql si existe
            Path seed = Path.of("seed_import.sql");
            if (Files.exists(seed)) {
                System.out.println("Ejecutando seed_import.sql ...");
                String sql = Files.lines(seed).collect(Collectors.joining("\n"));
                // separar por ';' simple (mejoraría para scripts complejos)
                for (String stmt : sql.split(";(?=\\s|$)")) {
                    String s = stmt.trim();
                    if (s.isEmpty()) continue;
                    try (Statement st = conn.createStatement()) {
                        st.execute(s);
                    }
                }
                conn.commit();
                System.out.println("seed_import.sql ejecutado correctamente.");
            } else {
                System.out.println("No se encontró seed_import.sql en el directorio actual: " + seed.toAbsolutePath());
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }
    }

    private static void dumpTableAsInserts(Connection conn, Writer out, String table) throws SQLException, IOException {
        String select = "SELECT * FROM " + table;
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            while (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO `").append(table).append("` (");
                for (int i = 1; i <= cols; i++) {
                    sb.append("`").append(md.getColumnName(i)).append("`");
                    if (i < cols) sb.append(",");
                }
                sb.append(") VALUES (");
                for (int i = 1; i <= cols; i++) {
                    Object v = rs.getObject(i);
                    if (v == null) {
                        sb.append("NULL");
                    } else if (v instanceof Number) {
                        sb.append(v.toString());
                    } else {
                        String s = v.toString().replace("\\", "\\\\").replace("'", "\\'");
                        sb.append("'").append(s).append("'");
                    }
                    if (i < cols) sb.append(",");
                }
                sb.append(");\n");
                out.write(sb.toString());
            }
        }
    }
}
