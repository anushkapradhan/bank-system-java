package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Admin {
	private Connection connection;
	private Scanner scanner;
	public Admin(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public boolean adminLogin() {
		scanner.nextLine();
		System.out.println();
		System.out.println("==== ADMIN LOGIN ====");
		System.out.print("Username: ");
		String username = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();
		
		if (username.equals("admin") && password.equals("admin")) {
			System.out.println("Admin logged in successfully!");
			System.out.println("------------------------------");
			return true;
		}else {
			System.out.println("Admin login failed! Incorrect credentials!");
			System.out.println("------------------------------");
			return false;
		}
	}
	
	public void getUsersList() {
		System.out.println();
		System.out.println("==== USERS LIST ====");
		
		try {
			String query = "SELECT * FROM user";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			String count = "SELECT COUNT(*) AS user_count FROM user";
			
			System.out.println("|        FULL NAME        |        EMAIL        |       PASSWORD       |");
			System.out.println("---------------------------------------------------------------------");
			while (resultSet.next()) {
	            System.out.println("|     " + resultSet.getString(1) + "     |     " + resultSet.getString(2) + "     |     " + resultSet.getString(3) + "     |");
	        }
			
	        ResultSet resultSetCount = statement.executeQuery(count);
			if (resultSetCount.next()) {
				int user_count = resultSetCount.getInt("user_count");
				System.out.println();
				System.out.println("Total Users: "+ user_count);
				System.out.println("------------------------------");
			}else {
				System.out.println("Total Users: 0");
				System.out.println("------------------------------");
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getAccountsList() {
		System.out.println();
		System.out.println("==== ACCOUNTS LIST ====");
		try {
			String userQuery = "SELECT * FROM User";
			String accountQuery = "SELECT * FROM Account WHERE email = ?";
			
			Statement statement = connection.createStatement();
			ResultSet userResultSet = statement.executeQuery(userQuery);
			
			System.out.println("|	FULL NAME	|	EMAIL	|	PASSWORD	|	ACCOUNT EXIST	|	ACCOUNT NUMBER	|");
			System.out.println("-------------------------------------------------------------------------------------------------------");
			
			while (userResultSet.next()) {
				String email = userResultSet.getString("email");
					
				PreparedStatement accountPreparedStatement = connection.prepareStatement(accountQuery);
				accountPreparedStatement.setString(1, email);
				ResultSet accountExist = accountPreparedStatement.executeQuery();
				
				String accountCreated = "";
				long accountNumber;
				if (accountExist.next()) {
					accountCreated = "YES";
					accountNumber = accountExist.getLong("account_number");
				}else {
					accountCreated = "NO";
					accountNumber = 0;
				}
				String full_name = userResultSet.getString("full_name");
				String password = userResultSet.getString("password");
				
				System.out.println("|	" + full_name + "	|	" + email + "	|	" + password + "	|	" + accountCreated + "	|	" + accountNumber + "	|");
			}
			
			System.out.println("-------------------------------------------------------------------------------------------------------");
			System.out.println("Please note: accounts with account number as 0 do not exist!");
			System.out.print("View details of any particular account? (Y/N): ");
			scanner.nextLine();
			String viewDetails = scanner.nextLine().toUpperCase();
			
			if (viewDetails.equals("Y") || viewDetails.equals("YES")) {
				System.out.println();
				System.out.print("Enter account number: ");
				long accountNoDetails = scanner.nextLong();
				if (accountNoDetails != 0) {
					accountDetails(accountNoDetails);
				}
			}else {
				System.out.println("Okay. Thank you!");
				System.out.println("------------------------------------------------------------------");
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void accountDetails(long account_number) {
		System.out.println();
		System.out.println("==== ACCOUNT DETAILS FOR ACCOUNT NUMBER: "+ account_number +" ====");
		try {
			String query = "SELECT * FROM ACCOUNT WHERE account_number = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, account_number);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String full_name = resultSet.getString("full_name");
				String email = resultSet.getString("email");
				double balance = resultSet.getDouble("balance");
				String security_pin = resultSet.getString("security_pin");
				
				System.out.println();
				System.out.println("ACCOUNT NUMBER: "+account_number);
				System.out.println("FULL NAME: "+ full_name);
				System.out.println("EMAIL: "+ email);
				System.out.println("CURRENT BALANCE: "+ balance);
				System.out.println("SECURITY PIN: "+ security_pin);
				System.out.println("------------------------------------------------------------------");
			}else {
				System.out.println("No such account exists. Please check the account number.");
				System.out.println("------------------------------------------------------------------");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
}
