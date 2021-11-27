package librarysystem;

import java.text.*;
import java.util.Date;
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

		try {
			String sql1 = "SELECT * FROM borrow WHERE callnum = ? AND copynum = ? AND returndate IS NULL";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, callNumber);
			st.setInt(2, copyNumber);
			ResultSet rs = st.executeQuery();
			if (!rs.next()) {
				String sql2 = "INSERT into borrow (callnum, copynum, libuid, checkout, returndate) VALUES (?, ?, ?, ?, NULL)";
				PreparedStatement st1 = conn.prepareStatement(sql2);
				st1.setString(1, callNumber);
				st1.setInt(2, copyNumber);
				st1.setString(3, userID);
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date date = new Date();
				date = new SimpleDateFormat("yyyy/MM/dd").parse(dateFormat.format(date));
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				st1.setDate(4, sqlDate);
				st1.executeUpdate();
				System.out.println("Book borrowing performed successfully!\n");
			} else {
				System.out.println("Sorry, the book cannot be borrowed!\n");
			}
		} catch (SQLException e) {
			System.out.println("Error encountered: Invalid input!\n");
		} catch (ParseException e) {
			System.out.println("Invalid date: " + e + "\n");
		}
	}

	public void ReturnBook(String userID1, String callNumber1, int copyNumber1, float rate) {

		try {
			// check if the book was borrowed by the library user

			String sql1 = "SELECT * FROM borrow WHERE callnum = ? AND copynum = ? AND  libuid = ?";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, callNumber1);
			st.setInt(2, copyNumber1);
			st.setString(3, userID1);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {

				// Update the return date
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date date = new Date();
				date = new SimpleDateFormat("yyyy/MM/dd").parse(dateFormat.format(date));
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				String sql2 = "UPDATE borrow SET returndate = ?  WHERE callnum = ? AND copynum = ? AND  libuid = ?";
				PreparedStatement st1 = conn.prepareStatement(sql2);
				st1.setDate(1, sqlDate);
				st1.setString(2, callNumber1);
				st1.setInt(3, copyNumber1);
				st1.setString(4, userID1);
				st1.executeUpdate();

				// Update the rating of the book
				String sql3 = "SELECT * FROM book WHERE callnum = ?";
				PreparedStatement st2 = conn.prepareStatement(sql3);
				st2.setString(1, callNumber1);
				ResultSet rs2 = st2.executeQuery();
				if (rs2.next()) {
					int oldTBorrowed = rs2.getInt("tborrowed");
					float oldRating = rs2.getFloat("rating");
					float newRating = ((oldRating * oldTBorrowed) + rate) / (oldTBorrowed + 1);
					String sql4 = "UPDATE book SET rating = ?  WHERE callnum = ?";
					PreparedStatement st3 = conn.prepareStatement(sql4);
					st3.setDouble(1, newRating);
					st3.setString(2, callNumber1);
					st3.executeUpdate();

					// Update the number of times the book is borrowed

					int newTBorrowed = oldTBorrowed + 1;
					String sql5 = "UPDATE book SET tborrowed = ?  WHERE callnum = ?";
					PreparedStatement st4 = conn.prepareStatement(sql5);
					st4.setInt(1, newTBorrowed);
					st4.setString(2, callNumber1);
					st4.executeUpdate();
				}

				System.out.println("Book returning performed successfully.\n");
			} else
				System.out.println("Unmatching data or The book has not been borrowed\n");

		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		} catch (ParseException e) {
			System.out.println("Invalid date: " + e + "\n");
		}

	}

	public void ListAllUnreturnedBooks(String date1, String date2) {
		try {
			String[] dateString1 = date1.split("/", -1);
			String dateNew = dateString1[2] + "/" + dateString1[1] + "/" + dateString1[0];
			Date date = new SimpleDateFormat("yyyy/MM/dd").parse(dateNew);
			java.sql.Date sqlDate1 = new java.sql.Date(date.getTime());
			dateString1 = date2.split("/", -1);
			dateNew = dateString1[2] + "/" + dateString1[1] + "/" + dateString1[0];
			date = new SimpleDateFormat("yyyy/MM/dd").parse(dateNew);
			java.sql.Date sqlDate2 = new java.sql.Date(date.getTime());

			String sql1 = "SELECT * FROM borrow WHERE returndate IS NULL AND checkout BETWEEN ? AND ? ORDER BY checkout DESC";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setDate(1, sqlDate1);
			st.setDate(2, sqlDate2);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				System.out.println("List of UnReturned Book:");
				System.out.println("|LibUID|CallNum|CopyNum|Checkout|");
				rs.previous();
				while (rs.next()) {
					System.out.print("|" + rs.getString("libuid") + "|" + rs.getString("callnum") + "|" + rs.getInt("copynum") + "|" + rs.getDate("checkout") + "|\n");
				}
				System.out.print("\n");
			} else
				System.out.println("No UnReturned Books within this time\n");
		} catch (SQLException e) {
			System.out.println("Error encounter: " + e.getMessage() + "\n");
		} catch (ParseException e) {
			System.out.println("Invalid date: " + e + "\n");
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
				System.out.print("Enter the call number: ");
				String callNumber = keyboard.nextLine();
				System.out.print("Enter the copy number: ");
				int copyNumber = keyboard.nextInt();
				BorrowBook(userID, callNumber, copyNumber);

				break;

			case 2:
				System.out.print("Enter the User ID: ");
				String userID1 = keyboard.nextLine();
				System.out.print("Enter the call number: ");
				String callNumber1 = keyboard.nextLine();
				System.out.print("Enter the copy number: ");
				int copyNumber1 = keyboard.nextInt();
				System.out.print("Enter Your Rating of the Book: ");
				float rate = keyboard.nextFloat();
				ReturnBook(userID1, callNumber1, copyNumber1, rate);

				break;

			case 3:
				System.out.print("Type in the starting date [dd/mm/yyyy]: ");
				String date1 = keyboard.nextLine();
				System.out.print("Type in the ending date [dd/mm/yyyy]: ");
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
