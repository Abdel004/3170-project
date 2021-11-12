package librarysystem;

import java.util.Scanner;

public class AdministratorMenu {

	int option;

	public AdministratorMenu() {
		this.option = 0;
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
		switch (option) {
		case 1:

			break;
		case 2:
			
			break;
		case 3:

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
