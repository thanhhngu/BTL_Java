package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    
    private static final String login = "sa";
    private static final String pass = "1234";

    private static final String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;" +
            "databaseName=QLDonHang;" +
            "encrypt=true;trustServerCertificate=true";;

 
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không load được driver", e);
        }
    }

  
    public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, login, pass);
    }

    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}