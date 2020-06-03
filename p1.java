import java.util.Scanner;

public class p1 {
//only modify P1 and Banking System

	public static void menu() {

		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		// String input = myObj.nextLine(); // Read user input
		String userID = "";
		boolean admScreen = false;
		while (true) {// Screen 1
			System.out.println("Title – Welcome to the Self Services Banking System! – Main Menu");
			System.out.println("1. New Customer");
			System.out.println("2. Customer Login");
			System.out.println("3. Exit");
			String input = myObj.nextLine(); // Read user input
			Boolean verifiedUser = false;

			if (input.equals("1")) { // Open account option
				String name = "";
				String gender = "";
				String age = "";
				String pin = "";
				System.out.println("Enter name:");
				name = myObj.nextLine(); // Read user input
				System.out.println("Enter gender:");
				gender = myObj.nextLine().toUpperCase();
				System.out.println("Age:");
				age = myObj.nextLine();
				System.out.println("Pin:");
				pin = myObj.nextLine();
				if (!gender.toUpperCase().equals("M") || !gender.toUpperCase().equals("F") && Integer.parseInt(pin) < 0
						&& Integer.parseInt(age) < 0) { // verifies if the input is valid
					System.out.println("Invalid input. Try again");
				} else {
					BankingSystem.newCustomer(name, gender.toUpperCase(), age, pin); // if valid it creates a new
																						// account
				}
			}
			if (input.equals("2")) { // Log in screen
				verifiedUser = false;
				String ID = "";
				String pin = "";
				System.out.println("Enter customer ID:");
				ID = myObj.nextLine(); // Read user input
				System.out.println("Enter pin:");
				pin = myObj.nextLine();
				if (ID.equals("0") && pin.equals("0")) {
					admScreen = true;
				} else {
					if (Integer.parseInt(pin) < 0 || Integer.parseInt(ID) < 100) { // verifies if the input is valid
						System.out.println("Invalid input. Try again");
					} else {
						verifiedUser = BankingSystem.VerifyCustomer(ID, pin); // If the input is valid it will log in,
																				// and jump to user screen
						userID = ID;
					}
				}

				if (admScreen) { // Administrator screen
					while (true) {
						System.out.println("Title – Administrator Main Menu- Select Number Choice(1-4)");
						System.out.println("1. Account Summary for a Customer");
						System.out
								.println("2. Report A :: Customer Information with Total Balance in Decreasing Order");
						System.out.println("3. Report B :: Find the Average Total Balance Between Age Groups");
						System.out.println("4. Exit");

						input = myObj.nextLine(); // Read user input

						if (input.equals("1")) {
							String newId = "";
							System.out.println("Enter customer ID:");
							newId = myObj.nextLine(); // Read user input
							if (Integer.parseInt(newId) < 100) { // verifies if the input is valid
								System.out.println("Invalid customerID. Try again");
							} else {
								BankingSystem.accountSummary(newId); // if valid input it will print summary
							}
						}

						if (input.equals("2")) {
							BankingSystem.reportA(); // prints report
						}

						if (input.equals("3")) {
							String min = "";
							String max = "";

							System.out.println("Enter min:");
							min = myObj.nextLine(); // Read user input
							System.out.println("Enter max:");
							max = myObj.nextLine();
							if (Integer.parseInt(min) < 0 || Integer.parseInt(max) < 0) { // verifies if input is valid
								System.out.println("Invalid age. Try again");
							} else {
								BankingSystem.reportB(min, max); // prints summary
							}
						}
						if (input.equals("4")) {
							admScreen=false;
							break;
						}
					}
				}
				if (verifiedUser) {
					while (true) {
						System.out.println("Title – Customer Main Menu");
						System.out.println("1. Open Account");
						System.out.println("2. Close Account");
						System.out.println("3. Deposit");
						System.out.println("4. Withdraw");
						System.out.println("5. Transfer ");
						System.out.println("6. Account Summary");
						System.out.println("7. Exit");

						input = myObj.nextLine(); // Read user input

						if (input.equals("1")) {
							String id = "";
							String type = "";
							String balance = "";
							System.out.println("Enter ID:");
							id = myObj.nextLine(); // Read user input
							System.out.println("Enter Account Type:");
							type = myObj.nextLine();
							System.out.println("Enter balance:");
							balance = myObj.nextLine();
							if ((!type.toUpperCase().equals("C") || !type.toUpperCase().equals("S"))
									&& Integer.parseInt(id) < 100 || Integer.parseInt(balance) <= 0) { // verifies if
																										// input is
																										// valid
								System.out.println("Invalid ID or balance. Try again");
							} else {
								BankingSystem.openAccount(id, type, balance); // Opens an account
							}

						}

						if (input.equals("2")) {

							String accountNumber = "";
							System.out.println("Enter Account Number:");

							accountNumber = myObj.nextLine();
							if (Integer.parseInt(accountNumber) < 1000) { // verifies if input is valid
								System.out.println("Invalid Account Number. Try again");
							} else {
								if (BankingSystem.findAccount(userID, accountNumber)) { // verifies if the user owns the
																						// account
									BankingSystem.closeAccount(accountNumber); // closes the account
								} else {
									System.out.println("The account does not match the user");
								}
							}
						}

						if (input.equals("3")) {

							String accountNumber = "";
							String deposit = "";
							System.out.println("Enter Account Number:");
							accountNumber = myObj.nextLine();
							System.out.println("Enter Deposit Ammount:");
							deposit = myObj.nextLine();
							if (Integer.parseInt(accountNumber) < 1000 || Integer.parseInt(deposit) <= 0) {// verifies
																											// if the
																											// data is
																											// valid
								System.out.println("Invalid Account Number or Deposit amount. Try again");
							} else {
								BankingSystem.deposit(accountNumber, deposit);// deposits money
							}

						}

						if (input.equals("4")) {

							String accountNumber = "";
							String widhdraw = "";
							System.out.println("Enter Account Number:");
							accountNumber = myObj.nextLine();
							System.out.println("Enter Wihtdraw Ammount:");
							widhdraw = myObj.nextLine();
							if (Integer.parseInt(accountNumber) < 1000 || Integer.parseInt(widhdraw) <= 0) { // validates
																												// input
								System.out.println("Invalid Account Number or Withdraw amount. Try again");
							} else {
								if (BankingSystem.findAccount(userID, accountNumber)) {// checks if the user owns the
																						// account
									BankingSystem.withdraw(accountNumber, widhdraw);// withdraws amount
								} else {
									System.out.println("The account does not match the user. Invalid to withdraw");
								}
							}
						}

						if (input.equals("5")) {

							String sourceNumber = "";
							String destinationNumber = "";
							String transferAmount = "";

							System.out.println("Enter Source Account Number:");
							sourceNumber = myObj.nextLine();
							System.out.println("Enter Destination Account Number:");
							destinationNumber = myObj.nextLine();
							System.out.println("Enter ammount:");
							transferAmount = myObj.nextLine();

							if (Integer.parseInt(sourceNumber) < 1000 || Integer.parseInt(destinationNumber) < 1000
									|| Integer.parseInt(transferAmount) <= 0) { // validates input
								System.out.println("Invalid Account Number or Transfer amount. Try again");
							} else {
								if (BankingSystem.findAccount(userID, sourceNumber)) { // checks if source account
																						// belongs to user
									BankingSystem.transfer(sourceNumber, destinationNumber, transferAmount);// transfer
																											// money
								} else {
									System.out.println("Invalid transaction. Accounts do not match");
								}
							}
						}

						if (input.equals("6")) {
							BankingSystem.accountSummary(userID);// prints summary
						}

						if (input.equals("7")) {
							verifiedUser=false;
							break; // exits user menu
						}

					}
				}

			}
			if (input.equals("3")) {
				System.out.println("Have a good day");
				break; // exits the program
			}

		}

		myObj.close();

	}

}
