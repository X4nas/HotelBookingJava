import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_booking?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // your DB username
    private static final String PASSWORD = "your_password"; // your DB password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
