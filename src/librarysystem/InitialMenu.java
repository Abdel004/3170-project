package librarysystem;
import java.sql.*;

public class InitialMenu {

	/**
	 * 
	 */
	public InitialMenu() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db56";
		String dbUsername = "Group56";
		String dbPassword = "Hellodb";
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
			conn.close();
			System.out.println("Sucsses!");
		} catch (ClassNotFoundException e) {
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

}
