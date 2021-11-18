package librarysystem;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

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
			System.out.println("Processing...Done. Database is initialized.\n");
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
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
			System.out.println("Processing...Done. Database is removed.\n");
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		} 
	}
	
	public void LoadData(String path) throws IOException {
		
	      File directoryPath = new File(System.getProperty("user.dir") + "/" + path);
	      Scanner sc = null;
	      boolean error = false;
	      if (directoryPath.list() != null) {
	    	  
	    	  File userCategories = new File(directoryPath + "/" + "user_category.txt");
	    	  sc = new Scanner(userCategories);
	    	  String input;
	    	  while (sc.hasNextLine()) {
		    	  try {
		    		  input = sc.nextLine();
		    		  String[] values = input.split("\t", -1);
		    		  int ucid = Integer.parseInt(values[0]);
		    		  int max = Integer.parseInt(values[1]);
		    		  int period = Integer.parseInt(values[2]);
		    		  PreparedStatement pstmt = this.conn.prepareStatement("INSERT INTO user_category(ucid,max,period) VALUES (?,?,?)");
		    		  pstmt.setInt(1, ucid);
		    		  pstmt.setInt(2, max);
		    		  pstmt.setInt(3, period);
		    		  pstmt.executeUpdate();
		    		  pstmt.close();
					} catch (SQLException e) {
						System.out.println("Problem occurred while inserting row: " + e.getMessage() + "\n");
	    	  			error = true;
						break;
					} 
	    	  }
	    	  
	    	  File users = new File(directoryPath + "/" + "user.txt");
	    	  sc = new Scanner(users);
	    	  while (sc.hasNextLine()) {
		    	  try {
		    		  input = sc.nextLine();
		    		  String[] values = input.split("\t", -1);
		    		  String libuid = values[0];
		    		  String name = values[1];
		    		  int age = Integer.parseInt(values[2]);
		    		  String address = values[3];
		    		  int ucid = Integer.parseInt(values[4]);
		    		  PreparedStatement pstmt = this.conn.prepareStatement("INSERT INTO libuser(libuid,name,age,address,ucid) VALUES (?,?,?,?,?)");
		    		  pstmt.setString(1, libuid);
		    		  pstmt.setString(2, name);
		    		  pstmt.setInt(3, age);
		    		  pstmt.setString(4, address);
		    		  pstmt.setInt(5, ucid);
		    		  pstmt.executeUpdate();
		    		  pstmt.close();
					} catch (SQLException e) {
						System.out.println("Problem occurred while inserting row: " + e.getMessage() + "\n");
						error = true;
						break;
					} 
	    	  }
	    	  
	    	  File bookCategories = new File(directoryPath + "/" + "book_category.txt");
	    	  sc = new Scanner(bookCategories);
	    	  while (sc.hasNextLine()) {
		    	  try {
			    	  input = sc.nextLine();
			    	  String[] values = input.split("\t", -1);
		    		  int bcid = Integer.parseInt(values[0]);
		    		  String bcname = values[1];
		    		  PreparedStatement pstmt = this.conn.prepareStatement("INSERT INTO book_category(bcid,bcname) VALUES (?,?)");
		    		  pstmt.setInt(1, bcid);
		    		  pstmt.setString(2, bcname);
		    		  pstmt.executeUpdate();
		    		  pstmt.close();
					} catch (SQLException e) {
						System.out.println("Problem occurred while inserting row: " + e.getMessage() + "\n");
						error = true;
						break;
					} 
	    	  }
	    	  
	    	  File books = new File(directoryPath + "/" + "book.txt");
	    	  sc = new Scanner(books);
	    	  while (sc.hasNextLine()) {
		    	  try {
			    	  input = sc.nextLine();
			    	  String[] values = input.split("\t", -1);
		    		  String callnum = values[0];
		    		  int copynum = Integer.parseInt(values[1]);
		    		  String title = values[2];
		    		  String[] authors = values[3].split(",", -1);
		    		  String[] dateString = values[4].split("/", -1);
		    		  try {
		    			  String date1 = dateString[2] + "/" + dateString[1] + "/" + dateString[0];
		    			  PreparedStatement pstmt = this.conn.prepareStatement("INSERT INTO book(callnum,title,publish,rating,tborrowed,bcid) VALUES (?,?,?,?,?,?)");
			    		  Date date = new SimpleDateFormat("yyyy/MM/dd").parse(date1);
			    		  java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			    		  if (values[5].equals("null")) {
			    			  pstmt.setString(4, null);
			    		  } else {
			    			  Double rating = Double.parseDouble(values[5]);
			    			  pstmt.setDouble(4, rating);
			    		  }
			    		  int tborrowed = Integer.parseInt(values[6]);
			    		  int bcid = Integer.parseInt(values[7]);
			    		  pstmt.setString(1, callnum);
			    		  pstmt.setString(2, title);
			    		  pstmt.setDate(3, sqlDate);
			    		  pstmt.setInt(5, tborrowed);
			    		  pstmt.setInt(6, bcid);
			    		  pstmt.executeUpdate();
			    		  pstmt.close();
		    			} catch (ParseException e) {
		    			    System.out.println("Invalid date: " + e + "\n");
		    			    break;
		    			}
		    		  for (int i = 1; i <= copynum; i++) {
			    		  PreparedStatement pstmt1 = this.conn.prepareStatement("INSERT INTO copy(callnum,copynum) VALUES (?,?)");
			    		  pstmt1.setString(1, callnum);
			    		  pstmt1.setInt(2, i);
			    		  pstmt1.executeUpdate();
			    		  pstmt1.close();
		    		  }
		    		  for (int i = 0; i < authors.length; i++) {
			    		  PreparedStatement pstmt2 = this.conn.prepareStatement("INSERT INTO authorship(aname,callnum) VALUES (?,?)");
			    		  pstmt2.setString(1, authors[i]);
			    		  pstmt2.setString(2, callnum);
			    		  pstmt2.executeUpdate();
			    		  pstmt2.close();
		    		  }
					} catch (SQLException e) {
						System.out.println("Problem occurred while inserting row: " + e.getMessage() + "\n");
						error = true;
						break;
					} 
	    	  }
	    	  
	    	  File records = new File(directoryPath + "/" + "check_out.txt");
	    	  sc = new Scanner(records);
	    	  while (sc.hasNextLine()) {
		    	  try {
			    	  input = sc.nextLine();
			    	  String[] values = input.split("\t", -1);
		    		  String callnum = values[0];
		    		  int copynum = Integer.parseInt(values[1]);
		    		  String libuid = values[2];
		    		  String[] dateString = values[3].split("/", -1);
		    		  try {
		    			  PreparedStatement pstmt = this.conn.prepareStatement("INSERT INTO borrow(libuid,callnum,copynum,checkout,returndate) VALUES (?,?,?,?,?)");
		    			  String date1 = dateString[2] + "/" + dateString[1] + "/" + dateString[0];
			    		  Date checkoutDate = new SimpleDateFormat("yyyy/MM/dd").parse(date1);
			    		  java.sql.Date checkoutSqlDate = new java.sql.Date(checkoutDate.getTime());
			    		  if (values[4].equals("null")) {
			    			  pstmt.setString(5, null);
			    		  } else {
			    			  String[] returnString = values[4].split("/", -1);
			    			  String date2 = returnString[2] + "/" + returnString[1] + "/" + returnString[0];
			    			  Date returnDate = new SimpleDateFormat("yyyy/MM/dd").parse(date2);
			    			  java.sql.Date returnSqlDate = new java.sql.Date(returnDate.getTime());
			    			  pstmt.setDate(5, returnSqlDate);
			    		  }
			    		  pstmt.setString(1, libuid);
			    		  pstmt.setString(2, callnum);
			    		  pstmt.setInt(3, copynum);
			    		  pstmt.setDate(4, checkoutSqlDate);
			    		  pstmt.executeUpdate();
			    		  pstmt.close();
		    			} catch (ParseException e) {
		    			    System.out.println("Invalid date: " + e + "\n");
		    			    break;
		    			}
					} catch (SQLException e) {
						System.out.println("Problem occurred while inserting row: " + e.getMessage() + "\n");
						error = true;
						break;
					} 
	    	  }
	    	  
	    	  if (!error) {
	    		  System.out.println("Processing...Done. Data is inputted to the database.\n");	
	    	  }  
	      } else {
	    	  throw new IOException("Folder does not exist!\n");
	      }

	}

	public void ShowRecords() {
		Statement stm;
		try {
			stm = this.conn.createStatement();
			String sql2 = "SELECT table_name "
					+ "FROM information_schema.tables "
					+ "WHERE table_schema = 'db56'";
			ResultSet rs = stm.executeQuery(sql2);
			if (!rs.next()) {
				System.out.println("No tables exist in the database.");
			} else {
				System.out.println("Number of records in each table:");
				rs.previous();
			}
			while(rs.next())
			{
				Statement queryStatement = this.conn.createStatement();
				String tableName = rs.getString(1);
				String sql3 = "SELECT COUNT(*) FROM " + tableName;
				ResultSet tableResults = queryStatement.executeQuery(sql3);
				tableResults.next();
				System.out.println(tableName + ": " + tableResults.getInt(1));
				queryStatement.close();
			}
			System.out.print("\n");
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		}

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
			try {
				LoadData(path);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			break;
		case 4:
			ShowRecords();
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
