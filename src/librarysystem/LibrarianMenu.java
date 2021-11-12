package librarysystem;

import java.util.Scanner;

public class LibrarianMenu {

	int option;

	public LibrarianMenu() {
		this.option = 0;
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
			option = keyboard.nextInt();
			switch (option) {
			case 1:

				break;
			case 2:

				break;
			case 3:
				
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
