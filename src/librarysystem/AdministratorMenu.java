package librarysystem;

import java.sql.*;
import java.util.Scanner;

public class AdministratorMenu {

	int option;
	Connection conn;
	
	public AdministratorMenu(Connection conn) {
		this.option = 0;
		this.conn = conn;
	}
	
	public void CreateTables() {
		try {
			Statement stm = this.conn.createStatement();
			String sql1 = "CREATE TABLE IF NOT EXISTS user_category("
					+ "ucid INT NOT NULL,"
					+ "max INT NOT NULL,"
					+ "period INT NOT NULL,"
					+ "PRIMARY KEY(ucid)"
					+ ")";
			String sql2 = "CREATE TABLE IF NOT EXISTS book_category("
					+ "bcid INT NOT NULL,"
					+ "bcname VARCHAR(30) NOT NULL,"
					+ "PRIMARY KEY(bcid)"
					+ ")";
			String sql3 = "CREATE TABLE IF NOT EXISTS libuser("
					+ "libuid CHAR(10) NOT NULL,"
					+ "name VARCHAR(25) NOT NULL,"
					+ "age INT NOT NULL,"
					+ "address VARCHAR(100) NOT NULL,"
					+ "ucid INT NOT NULL,"
					+ "PRIMARY KEY(libuid),"
					+ "FOREIGN KEY(ucid) REFERENCES user_category(ucid) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ")";
			String sql4 = "CREATE TABLE IF NOT EXISTS book("
					+ "callnum CHAR(8) NOT NULL,"
					+ "title VARCHAR(30) NOT NULL,"
					+ "publish DATE NOT NULL, "
					+ "rating REAL,"
					+ "tborrowed INT DEFAULT '0' NOT NULL,"
					+ "bcid INT NOT NULL,"
					+ "PRIMARY KEY(callnum),"
					+ "FOREIGN KEY(bcid) REFERENCES book_category(bcid) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ")";
			String sql5 = "CREATE TABLE IF NOT EXISTS copy("
					+ "callnum CHAR(8) NOT NULL,"
					+ "copynum INT NOT NUll,"
					+ "PRIMARY KEY(callnum, copynum),"
					+ "FOREIGN KEY(callnum) REFERENCES book(callnum) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ")";
			String sql6 = "CREATE TABLE IF NOT EXISTS borrow("
					+ "libuid CHAR(10) NOT NULL,"
					+ "callnum CHAR(8) NOT NULL,"
					+ "copynum INT NOT NUll,"
					+ "checkout DATE NOT NULL,"
					+ "returndate DATE,"
					+ "PRIMARY KEY(libuid, callnum, copynum, checkout),"
					+ "FOREIGN KEY(callnum, copynum) REFERENCES copy(callnum, copynum) ON DELETE CASCADE ON UPDATE CASCADE,"
					+ "FOREIGN KEY(libuid) REFERENCES libuser(libuid) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ")";
			String sql7 = "CREATE TABLE IF NOT EXISTS authorship("
					+ "aname VARCHAR(25) NOT NULL,"
					+ "callnum CHAR(8) NOT NULL,"
					+ "PRIMARY KEY(aname, callnum),"
					+ "FOREIGN KEY(callnum) REFERENCES book(callnum) ON DELETE CASCADE ON UPDATE CASCADE"
					+ ")";
			stm.addBatch(sql1);
			stm.addBatch(sql2);
			stm.addBatch(sql3);
			stm.addBatch(sql4);
			stm.addBatch(sql5);
			stm.addBatch(sql6);
			stm.addBatch(sql7);
			stm.executeBatch();
			stm.close();
			System.out.println("Processing...Done. Database is initialized.");
			System.out.print("\n");
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage());
			System.out.print("\n");
		}
	}
	
	public void DeleteTables() {
		try {
			Statement stm = this.conn.createStatement();
			String sql1 = "SET FOREIGN_KEY_CHECKS = 0";
			String sql2 = "SELECT table_name "
					+ "FROM information_schema.tables "
					+ "WHERE table_schema = 'db56'";
			String sql4 = "SET FOREIGN_KEY_CHECKS = 1";
			stm.execute(sql1);
			ResultSet rs = stm.executeQuery(sql2);
			while(rs.next())
			{
				Statement updateStatement = this.conn.createStatement();
				String tableName = rs.getString(1);
				String sql3 = "DROP TABLE IF EXISTS " + tableName;
				updateStatement.executeUpdate(sql3);
				updateStatement.close();
			}
			rs.close();
			stm.execute(sql4);
			stm.close();
			System.out.println("Processing...Done. Database is removed.");
			System.out.print("\n");
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage());
			System.out.print("\n");
		} 
	}
	
	public void LoadData(String path) {
		
	}

	public void ShowAdministratorMenu() {
		System.out.print("\n");
		while (true) {
		System.out.println("-----Operations for administrator menu-----");
		System.out.println("What kind of operation would you like to perform?");
		System.out.println("1. Create all tables");
		System.out.println("2. Delete all tables");
		System.out.println("3. Load from datafile");
		System.out.println("4. Show number of records in each table");
		System.out.println("5. Return to the main menu");
		System.out.print("Enter your choice: ");
		Scanner keyboard = new Scanner(System.in);
		option = keyboard.nextInt();
		keyboard.nextLine();
		switch (option) {
		case 1:
			CreateTables();
			break;
		case 2:
			DeleteTables();
			break;
		case 3:
			System.out.println("\nType in the Source Data Folder Path: ");
			String path = keyboard.nextLine();
			LoadData(path);
			break;
		case 4:
			
			break;
		case 5:
			System.out.print("\n");
			return;
		default:
			System.out.println("Invalid Choice! Please try again with a valid choice!\n");
		}
	}
	}

}
