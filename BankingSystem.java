import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;

	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * 
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties(); // Create a new Properties object
			FileInputStream input = new FileInputStream(filename); // Create a new FileInputStream object using our
																	// filename parameter
			props.load(input); // Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver"); // Load the driver
			url = props.getProperty("jdbc.url"); // Load the url
			username = props.getProperty("jdbc.username"); // Load the username
			password = props.getProperty("jdbc.password"); // Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
			e.printStackTrace();
		}
	}

	/**
	 * Create a new customer.
	 * 
	 * @param name   customer name
	 * @param gender customer gender
	 * @param age    customer age
	 * @param pin    customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) {

		try {
			Class.forName(driver); // load the driver
			Connection con = DriverManager.getConnection(url, username, password); // Create the connection
			// Statement stmt = con.createStatement(); //Create a statement

			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO P1.Customer (Name, Gender, Age, PIN)" + "VALUES (?, ?, ?,?)"); // Insertion
																													// statement
			// PreparedStatement stmtID =stmt.RETURN_GENERATED_KEYS;
			stmt.setString(1, name);
			stmt.setString(2, gender);
			stmt.setString(3, age);
			stmt.setString(4, pin);
			stmt.executeUpdate();
			stmt.close(); // Close the statement after we are done with the statement

			Statement statement = con.createStatement();
			String query = "SELECT Max(ID) FROM P1.Customer";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			String newID = rs.getString(1);
			System.out.println("New user id: " + newID); // returns new user ID

			con.close();
			// Close the connection after we are done with everything
		} catch (Exception e) {
			System.out.println("Exception adding new Customer");
			e.printStackTrace();
		}

	}

	/**
	 * Verifies if the user is correct
	 * 
	 * @param ID  user Id
	 * @param pin user pin
	 * @return if the user matches or not
	 */
	public static Boolean VerifyCustomer(String ID, String pin) {
		Boolean verify = false;
		try {
			Class.forName(driver); // load the driver
			Connection con = DriverManager.getConnection(url, username, password);
			Statement stmt = con.createStatement();
			String query = "SELECT ID, pin from P1.Customer"; // selection
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) { // gets the contents of each row
				String tempID = rs.getString(1);
				String tempPin = rs.getString(2);
				if (ID.equals(tempID) && pin.equals(tempPin)) {
					System.out.println("Valid login information");
					verify = true;
					break;
				}
			}
			if (!verify) {
				System.out.println("Invalid user, try again"); // prints if the user does not match

			}

			rs.close(); // Close the statement after we are done with the statement
			con.close();
			return verify;
			// Close the connection after we are done with everything
		} catch (Exception e) {
			System.out.println("Exception validating Customer");
			e.printStackTrace();
		}

		return verify;

	}

	/**
	 * Open a new account.
	 * 
	 * @param id     customer id
	 * @param type   type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) {

		try {
			Class.forName(driver); // load the driver
			Connection con = DriverManager.getConnection(url, username, password); // Create the connection
			// Statement stmt = con.createStatement(); //Create a statement

			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO P1.Account (ID, Balance, Type, Status)" + "VALUES (?, ?, ?,?)"); // Insertion
																													// statement
			stmt.setString(1, id);
			stmt.setString(2, amount);
			stmt.setString(3, type);
			stmt.setString(4, "A");
			stmt.executeUpdate();
			stmt.close(); // Close the statement after we are done with the statement

			Statement statement = con.createStatement();
			String query = "SELECT Max(Number) FROM P1.Account";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			String newID = rs.getString(1);
			System.out.println("New account id: " + newID);

			con.close();
			// Close the connection after we are done with everything
		} catch (Exception e) {
			System.out.println("Exception adding new Customer");
			e.printStackTrace();
		}

	}

	/**
	 * Close an account.
	 * 
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) {
		Boolean verify = false;
		try {
			Class.forName(driver); // load the driver
			Connection con = DriverManager.getConnection(url, username, password); // Create the connection
			// Statement stmt = con.createStatement(); //Create a statement
			Statement stmt = con.createStatement();
			String query = "SELECT  Number from P1.Account "; // Insertion statement
			ResultSet rs = stmt.executeQuery(query); // Executing the query and storing the results in a Result Set

			while (rs.next()) { // Loop through result set and retrieve contents of each row
				String tempAccountNumber = rs.getString(1);

				if (accNum.equals(tempAccountNumber)) {
					verify = true;
					break;
				}

			}
			if (verify) {
				String updateNumber = "UPDATE P1.Account set status = ? WHERE Number = ?"; // Insertion statement
				PreparedStatement preparedStmt = con.prepareStatement(updateNumber);
				preparedStmt.setString(1, "I");
				preparedStmt.setInt(2, Integer.parseInt(accNum));
				preparedStmt.executeUpdate();

				String zeroBalance = "UPDATE P1.Account set balance = ? WHERE Number = ?"; // Insertion statement
				PreparedStatement preparedStmtBalance = con.prepareStatement(zeroBalance);
				preparedStmtBalance.setString(1, "0");
				preparedStmtBalance.setInt(2, Integer.parseInt(accNum));
				preparedStmtBalance.executeUpdate();

				verify = true;
				System.out.println(":: CLOSE ACCOUNT - SUCCESS");

			} else {
				System.out.println("Invalid Account number, Try again");
			}

			rs.close(); // Close the statement after we are done with the statement
			con.close();

			// Close the connection after we are done with everything
		} catch (Exception e) {
			System.out.println("Exception validating Account Number");
			e.printStackTrace();
		}

	}

	/**
	 * Deposit into an account.
	 * 
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) {
		Boolean verify = false;
		try {
			Class.forName(driver); // load the driver
			Connection con = DriverManager.getConnection(url, username, password); // Create the connection
			// Statement stmt = con.createStatement(); //Create a statement
			Statement stmt = con.createStatement();
			String query = "SELECT  Number, balance  from P1.Account "; // Insertion statement
			ResultSet rs = stmt.executeQuery(query); // Executing the query and storing the results in a Result Set
			String oldAmount = "";
			while (rs.next()) { // Loop through result set and retrieve contents of each row
				String tempAccountNumber = rs.getString(1);

				if (accNum.equals(tempAccountNumber)) {
					oldAmount = rs.getString(2);
					verify = true;
					break;
				}

			}
			if (verify) {
				String updateNumber = "UPDATE P1.Account set balance = ? WHERE Number = ?"; // Insertion statement
				PreparedStatement preparedStmt = con.prepareStatement(updateNumber);

				preparedStmt.setString(1, Integer.toString(Integer.parseInt(oldAmount) + Integer.parseInt(amount)));
				preparedStmt.setInt(2, Integer.parseInt(accNum));
				preparedStmt.executeUpdate();

				verify = true;
				System.out.println(":: DEPOSIT - SUCCESS");

			} else {
				System.out.println("Invalid Account number, Try again");
			}

			rs.close(); // Close the statement after we are done with the statement
			con.close();

			// Close the connection after we are done with everything
		} catch (Exception e) {
			System.out.println("Exception validating Account Number");
			e.printStackTrace();
		}

	}

	/**
	 * Withdraw from an account.
	 * 
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) {
		Boolean verify = false;
		try {
			Class.forName(driver); // load the driver
			Connection con = DriverManager.getConnection(url, username, password); // Create the connection
			// Statement stmt = con.createStatement(); //Create a statement
			Statement stmt = con.createStatement();
			String query = "SELECT  Number, balance  from P1.Account "; // Insertion statement
			ResultSet rs = stmt.executeQuery(query); // Executing the query and storing the results in a Result Set
			String oldAmount = "";
			while (rs.next()) { // Loop through result set and retrieve contents of each row
				String tempAccountNumber = rs.getString(1);

				if (accNum.equals(tempAccountNumber)) {
					oldAmount = rs.getString(2);
					verify = true;
					break;
				}

			}
			if (verify) {
				String updateNumber = "UPDATE P1.Account set balance = ? WHERE Number = ?"; // Insertion statement
				PreparedStatement preparedStmt = con.prepareStatement(updateNumber);

				preparedStmt.setString(1, Integer.toString(Integer.parseInt(oldAmount) - Integer.parseInt(amount)));
				preparedStmt.setInt(2, Integer.parseInt(accNum));
				preparedStmt.executeUpdate();

				verify = true;
				System.out.println(":: WITHDRAW - SUCCESS");

			} else {
				System.out.println("Invalid Account number, Try again");
			}

			rs.close(); // Close the statement after we are done with the statement
			con.close();

			// Close the connection after we are done with everything
		} catch (Exception e) {
			System.out.println("Exception validating Account Number");
			e.printStackTrace();
		}
	}

	/**
	 * Transfer amount from source account to destination account.
	 * 
	 * @param srcAccNum  source account number
	 * @param destAccNum destination account number
	 * @param amount     transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) {
		withdraw(srcAccNum, amount);// withdraws amount from source account
		deposit(destAccNum, amount);// deposit amount into destination account
	}

	/**
	 * Display account summary.
	 * 
	 * @param cusID customer ID
	 */
	public static void accountSummary(String cusID) {
		try {
			rs = null;
			String output = ":: ACCOUNT SUMMARY - FAILURE\n";
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = String.format("SELECT * FROM P1.ACCOUNT WHERE (ID=%s AND STATUS='A')", cusID);
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				output = String.format(":: ACCOUNT SUMMARY FOR CUSTOMER #%s - SUCCESS\n", cusID);
				output += String.format("%-6s %-12s", "NUMBER", "BALANCE");
				System.out.println(output);
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					output = String.format("%-6s %-12s", rs.getString(1), rs.getString(3));
					System.out.println(output);
				}
			} else {
				System.out.println(":: ACCOUNT SUMMARY - FAILURE\n");
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: ACCOUNT SUMMARY - FAILURE");
			System.out.println(err);
			System.out.println();
		}

	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing
	 * Order.
	 */
	public static void reportA() {
		try {
			String output = ":: REPORT A - FAILURE\n";
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = "SELECT * FROM TOTAL_BALANCE";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				rs = stmt.executeQuery(query);
				output = ":: REPORT A - SUCCESS\n";
				output += String.format("%-5s %-12s %-4s %-6s %-12s\n", "ID", "NAME", "AGE", "GENDER", "BALANCE"); // selection
																													// statement
				System.out.println(output);
				while (rs.next()) {
					output = String.format("%-5s %-12s %-4s %-6s %-12s", rs.getString(1), rs.getString(2),
							rs.getString(3), rs.getString(4), rs.getString(5));
					System.out.println(output);
				}
			} else {
				System.out.println(output);
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: REPORT A - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing
	 * Order.
	 * 
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) {
		try {
			rs = null;
			String output = ":: REPORT B - FAILURE\n";
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = String.format("SELECT AVG(BALANCE) FROM TOTAL_BALANCE WHERE AGE>=%s AND AGE<=%s", min, max); // selection
																														// statement
			rs = stmt.executeQuery(query);
			if (rs != null) {
				output = ":: REPORT B - SUCCESS";
				System.out.println(output);
			}
			while (rs.next()) {
				output = String.format("Average Total Balance For Age %s to %s: %s", min, max, rs.getString(1));
				System.out.println(output);
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: REPORT B - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * will verify if the user(ID) owns the account number
	 * 
	 * @param id     customerId
	 * @param number account number
	 */
	public static boolean findAccount(String id, String number) {
		try {
			rs = null;
			String output = ":: VERIFYING ACCOUNT - FAILURE\n";
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			String query = String.format("SELECT * FROM P1.ACCOUNT WHERE(ID=%s AND NUMBER=%s AND STATUS='A')", id,
					number); // selection statement
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				output = "Verified account-success";
				System.out.println(output);
				return true;
			} else {
				System.out.println(output);
			}
			con.close();
		} catch (Exception err) {
			System.out.println("The account could not be verified");
			System.out.println(err);
		}
		return false;
	}
}
