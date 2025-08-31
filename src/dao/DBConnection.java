package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Método para imprimir las tablas de la base de datos
    public static void printTables() {
        try (Connection conn = getConnection()) {
            var meta = conn.getMetaData();
            var rs = meta.getTables(null, null, "%", new String[] {"TABLE"});
            System.out.println("Tablas en la base de datos:");
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las tablas: " + e.getMessage());
        }
    }
    private static final String URL = "jdbc:postgresql://db.eadbhqtppxqorwtbhkes.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ElErickesmiDios69";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
        // Método de prueba para verificar la conexión
        public static void testConnection() {
            try (Connection conn = getConnection()) {
                if (conn != null && !conn.isClosed()) {
                    System.out.println("Conexión exitosa a PostgreSQL Supabase");
                } else {
                    System.out.println("No se pudo conectar a la base de datos");
                }
            } catch (SQLException e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        }
}
