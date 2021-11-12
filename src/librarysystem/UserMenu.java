package librarysystem;

import java.util.Scanner;

public class UserMenu {

	int option;

	public UserMenu() {
		this.option = 0;
	}

	public void ShowUserMenu() {
		System.out.print("\n");
		while (true) {
			System.out.println("-----Operations for library user menu-----");
			System.out.println("What kind of operation would you like to perform?");
			System.out.println("1. Search for Books");
			System.out.println("2. Show loan record of a user");
			System.out.println("3. Return to the main menu");
			System.out.print("Enter your choice: ");
			Scanner keyboard = new Scanner(System.in);
			option = keyboard.nextInt();
			switch (option) {
			case 1:

				break;
			case 2:

				break;
			case 3:
				System.out.print("\n");
				return;
			default:
				System.out.println("Invalid Choice! Please try again with a valid choice!\n");
			}
		}
	}

}
