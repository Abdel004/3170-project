package librarysystem;

import java.io.IOException;
import java.util.*;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.Scanner;

public class LibrarianMenu {

	int option;
	Connection conn;
	
	public LibrarianMenu(Connection conn) {
		this.option = 0;
		this.conn = conn;
	}
	
	public void BorrowBook(String userID, String callNumber, int copyNumber) {
		Statement stm;
		
			try {
			stm = this.conn.createStatement();
			
			String sql1 = "SELECT * FROM check_out WHERE callnum = ? AND copynum = ? AND return = ''";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, callNumber);
			st.setInt(2, copyNumber);
			ResultSet rs = st.executeQuery(sql1);
		   if (!rs.next() )
		   {
			 String sql2= "INSERT into check_out (callnum, copynum, libuid, checkout, returm) VALUES (?, ?, ?, ?, '')";
				PreparedStatement st1 = conn.prepareStatement(sql2);
				st1.setString(1, callNumber);
				st1.setInt(2, copyNumber);
				st1.setString(3, userID);
				String pattern = "dd-MM-yyyy";
				DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate time = LocalDate.now();
				st1.setString(4, date.format(time));
				ResultSet rs1 = st.executeQuery(sql2);
				System.out.println("Book borrowing performed successfully!\n");
		   }
		   else 
				System.out.println("Sorry, the book cannot be borrowed!");
			} catch (SQLException e) {
				System.out.println("Error encounter: " + e.getMessage() + "\n");
			}	
	}
	
	
	public void ReturnBook(String userID1, String callNumber1, int copyNumber1, float rate) {
		Statement stm;
		
		try {
			stm = this.conn.createStatement();
			
			//check if the book was borrowed by the library user
			
			String sql1 = "SELECT * FROM check_out WHERE callnum = ? AND copynum = ? AND  libuid = ?";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, callNumber1);
			st.setInt(2, copyNumber1);
			st.setString(3, userID1);
			ResultSet rs = st.executeQuery(sql1);
			if (rs.next()) {
				
				//Update the return date
			
				DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate time = LocalDate.now();
				String sql2 = "UPDATE check_out SET return = date.format(time)  WHERE callnum = ? AND copynum = ? AND  libuid = ?";
				st.setString(1, callNumber1);
				st.setInt(2, copyNumber1);
				st.setString(3, userID1);	
				ResultSet rs1 = st.executeQuery(sql2);
				
				//Update the rating of the book
				String sql3 = "SELECT * FROM book WHERE callnum = ? AND copynum = ?";
				st.setString(1, callNumber1);
				st.setInt(2, copyNumber1);
				ResultSet rs2 = st.executeQuery(sql3);
				if (rs.next()) {
					int oldTBorrowed = rs.getInt("tborrowed");
					float oldRating = rs.getFloat("rating");
					float newRating = ((oldRating*oldTBorrowed)+rate)/(oldTBorrowed +1);
					String sql4 = "UPDATE book SET rating = newRating  WHERE callnum = ? AND copynum = ?";
					st.setString(1, callNumber1);
					st.setInt(2, copyNumber1);
					ResultSet rs3 = st.executeQuery(sql4);
					
					//Update the number of times the book is borrowed
					
					int newTBorrowed = oldTBorrowed + 1;
					String sql5 = "UPDATE book SET tborrowed = newTBorrowed  WHERE callnum = ? AND copynum = ?";
					st.setString(1, callNumber1);
					st.setInt(2, copyNumber1);
					ResultSet rs4 = st.executeQuery(sql5);
				}
				
				System.out.println("Book returning performed successfully.");			
			}
			else 
				System.out.println("Unmatching data or The book has not been borrowed");
			
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		}
		
	}
	
	
	public void ListAllUnreturnedBooks(String date1,String  date2){
		Statement stm;
		try {
			stm = this.conn.createStatement();
			
			//make the whole data table in descending order
			
			String sql1 = "SELECT * FROM check_out ORDER BY checkout DESC";
			stm.execute(sql1);
			
			//select the rows where books are unreturned within the wanted period of time 
			
			String sql2 = "SELECT * FROM check_out WHERE return ='' AND checkout BETWEEN date1 AND date2";
			ResultSet rs = stm.executeQuery(sql2);
			if (rs.next()) {
				System.out.println("List of UnReturned Book:");
				System.out.println("|LibUID|CallNum|CopyNum|Checkout|");
				 while(rs.next()) {
			         System.out.print("|"+rs.getString("libuid")+"|"+ rs.getString("callnum")+rs.getString("copynum")+"|"
				 +rs.getString("checkout")+"|");
			         stm.close();
			      }
				 System.out.print("\n");
					rs.close();
			} else 
				System.out.println("No UnReturned Books within this time");			
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		}
		
	}

	
	public void ShowLibrarianMenu() {
		System.out.print("\n");
		while (true) {
			System.out.println("-----Operations for librarian menu-----");
			System.out.println("What kind of operation would you like to perform?");
			System.out.println("1. Book Borrowing");
			System.out.println("2. Book Returning");
			System.out.println("3. List all un-returned book copies which are checked-out within a period");
			System.out.println("4. Return to the main menu");
			System.out.print("Enter your choice: ");
			Scanner keyboard = new Scanner(System.in);
			String optionString = keyboard.nextLine();
			option = Integer.parseInt(optionString);
			
			switch (option) {
			case 1:
				System.out.print("Enter the User ID: ");
				String userID = keyboard.nextLine();
				System.out.print("\nEnter the call number: ");
				String callNumber = keyboard.nextLine();
				System.out.print("\nEnter the copy number: ");
				int copyNumber = keyboard.nextInt();
				BorrowBook(userID,callNumber,copyNumber);
				
				break;
				
			case 2:
				System.out.print("Enter the User ID: ");
				String userID1 = keyboard.nextLine();
				System.out.print("\nEnter the call number: ");
				String callNumber1 = keyboard.nextLine();
				System.out.print("\nEnter the copy number: ");
				int copyNumber1 = keyboard.nextInt();
				System.out.print("\nEnter Your Rating of the Book: ");
				float rate = keyboard.nextFloat();
				ReturnBook(userID1, callNumber1, copyNumber1, rate);
				
				break;
				
			case 3:
				System.out.print("Type in the starting date [dd/mm/yyyy]: ");
				String date1 = keyboard.nextLine();
				System.out.print("\nType in the ending date [dd/mm/yyyy]: ");
				String date2 = keyboard.nextLine();
				ListAllUnreturnedBooks(date1, date2);
				break;
				
			case 4:
				System.out.print("\n");
				return;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			}
		}
	}

}
