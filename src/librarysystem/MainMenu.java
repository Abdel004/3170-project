package librarysystem;

import java.sql.*;
import java.util.Scanner;

public class MainMenu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db56";
		String dbUsername = "Group56";
		String dbPassword = "Hellodb";
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
		} catch (ClassNotFoundException e) {
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		int option = 0;
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Welcome to Library Inquiry System!\n");
		while (true) {
			System.out.println("-----Main menu-----");
			System.out.println("What kinds of operations would you like to perform?");
			System.out.println("1. Operations for Administrator");
			System.out.println("2. Operations for Library User");
			System.out.println("3. Operations for Librarian");
			System.out.println("4. Exit this program");
			System.out.print("Enter your choice: ");
			option = keyboard.nextInt();
			switch (option) {
			case 1:
				AdministratorMenu adminMenu = new AdministratorMenu(conn);
				adminMenu.ShowAdministratorMenu();
				break;
			case 2:
				UserMenu userMenu = new UserMenu(conn);
				userMenu.ShowUserMenu();
				break;
			case 3:
				LibrarianMenu librarianMenu = new LibrarianMenu(conn);
				librarianMenu.ShowLibrarianMenu();
				break;
			case 4:
				System.out.println("Goodbye!");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			}
		}

	}

}
