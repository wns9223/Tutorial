import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
	public static Connection makeConnection() {
		final String url = "jdbc:mysql://localhost/test";
		final String id = "root";
		final String password = "123";
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver Success");
			con = (Connection) DriverManager.getConnection(url, id, password);
			System.out.println("DateBase Connect Success.");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Driver not");
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("connected is fail");
		}
		return con;
	}
}
