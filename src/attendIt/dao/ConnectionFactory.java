package attendIt.dao;

import java.sql.*;

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/attendIt";
    private static final String USER = "postgres";
    private static final String PASS = "senha";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
