package BankingManagementSystem;

//import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class AccountManager {
	private Connection connection;
	private Scanner scanner;
	AccountManager(Connection connection, Scanner scanner){
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void credit_money(long account_number) throws SQLException{
		scanner.nextLine();
		System.out.println();
		System.out.println("==== CREDIT MONEY ====");
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Account WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if (resultSet.next()) {
					String credit_query = "UPDATE Account SET balance = balance + ? WHERE account_number = ?";
					PreparedStatement preparedStatementCredit = connection.prepareStatement(credit_query);
					preparedStatementCredit.setDouble(1, amount);
					preparedStatementCredit.setLong(2, account_number);
					int affectedRows = preparedStatementCredit.executeUpdate();
					
					if (affectedRows > 0) {
						System.out.println("Rs. "+ amount + " credited successfully!");
						System.out.println("------------------------------");
						connection.commit();
						connection.setAutoCommit(true);
						return;
					}else {
						System.out.println("Transaction failed!");
						System.out.println("------------------------------");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				}else {
					System.out.println("Invalid Security Pin!");
					System.out.println("------------------------------");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
	
	public void debit_money(long account_number) throws SQLException{
		scanner.nextLine();
		System.out.println();
		System.out.println("==== DEBIT MONEY ====");
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Account WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if (resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if (amount <= current_balance) {
						String debit_query = "UPDATE Account SET balance = balance - ? WHERE account_number = ?";
						PreparedStatement preparedStatementDebit = connection.prepareStatement(debit_query);
						preparedStatementDebit.setDouble(1, amount);
						preparedStatementDebit.setLong(2, account_number);
						int affectedRows = preparedStatementDebit.executeUpdate();
						
						if (affectedRows > 0) {
							System.out.println("Rs. "+ amount + " debited successfully!");
							System.out.println("------------------------------");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						}else {
							System.out.println("Transaction Failed!");
							System.out.println("------------------------------");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					}else {
						System.out.println("Insufficient Balance!");
						System.out.println("------------------------------");
					}
				}else {
					System.out.println("Invalid Pin!");
					System.out.println("------------------------------");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
	
	public void transfer_money(long sender_account_number) throws SQLException{
		scanner.nextLine();
		System.out.println();
		System.out.println("==== TRABSFER MONEY ====");
		System.out.print("Enter Receiver Account Number: ");
		long receiver_account_number = scanner.nextLong();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			//System.out.print("inside try");
			connection.setAutoCommit(false);
			if (sender_account_number != receiver_account_number) {
				if (sender_account_number != 0 && receiver_account_number != 0) {
					PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Account WHERE account_number = ? AND security_pin = ?");
					preparedStatement.setLong(1, sender_account_number);
					preparedStatement.setString(2, security_pin);
					ResultSet resultSet = preparedStatement.executeQuery();
					
					if (resultSet.next()) {
						double current_balance = resultSet.getDouble("balance");
						if (amount <= current_balance) {
							String debit_query = "UPDATE Account SET balance = balance - ? WHERE account_number = ?";
							String credit_query = "UPDATE Account SET balance = balance + ? WHERE account_number = ?";
							
							PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
							PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
							
							creditPreparedStatement.setDouble(1, amount);
							creditPreparedStatement.setLong(2, receiver_account_number);
							
							debitPreparedStatement.setDouble(1, amount);
							debitPreparedStatement.setLong(2, sender_account_number);
							
							int affectedRowsCredit = creditPreparedStatement.executeUpdate();
							int affectedRowsDebit = debitPreparedStatement.executeUpdate();
							
							if (affectedRowsCredit > 0 && affectedRowsDebit > 0) {
								System.out.println("Transaction Successfully!");
								System.out.println("Rs. "+ amount + " Transferred Successfully!");
								System.out.println("------------------------------");
								connection.commit();
								connection.setAutoCommit(true);
								return;
							}else {
								System.out.println("Transaction Failed!");
								System.out.println("------------------------------");
								connection.rollback();
								connection.setAutoCommit(true);
							}
						}else {
							System.out.println("Insufficient Balance!");
							System.out.println("------------------------------");
						}
					}else {
						System.out.println("Invalid Security Pin!");
						System.out.println("------------------------------");
					}
				} else {
					System.out.println("Invalid account number!");
					System.out.println("------------------------------");
				}
			}else {
				System.out.println("You cannot transfer money to yourself from the same account!");
				System.out.println("------------------------------");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
	
	public void getBalance(long account_number) {
		scanner.nextLine();
		System.out.println();
		System.out.println("==== CHECK BALANCE ====");
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM Account WHERE account_number = ? AND security_pin = ?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				double balance = resultSet.getDouble("balance");
				System.out.println();
				System.out.println("Account Number: "+ account_number);
				System.out.println("Current Balance: "+ balance);
				System.out.println("------------------------------");
			}else {
				System.out.println("Invalid Pin!");
				System.out.println("------------------------------");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getAccountInfo(long account_number) {
		scanner.nextLine();
		System.out.println();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			
			String query = "SELECT * FROM Account WHERE account_number = ? AND security_pin = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				String full_name = resultSet.getString("full_name");
				String email = resultSet.getString("email");
				double balance = resultSet.getDouble("balance");
				
				System.out.println();
				System.out.println("==== ACCOUNT INFORMATION ====");
				System.out.println("Account Number: " + account_number);
				System.out.println("Full Name: "+ full_name);
				System.out.println("Email: "+ email);
				System.out.println("Balance: "+ balance);
				String security_pin_star = "";
				int security_pin_length = security_pin.length();
				while(security_pin_length > 0) {
					security_pin_star = security_pin_star + "*";
					security_pin_length -= 1;
				}
				System.out.println("Security Pin: "+ security_pin_star);
				System.out.println("------------------------------");
				System.out.print("Change Security Pin? (Y/N): ");
				String changePin = scanner.nextLine().toUpperCase();
				
				if (changePin.equals("Y") || changePin.equals("YES")) {
					System.out.println();
					System.out.println("==== CHANGE SECURITY PIN ====");
					System.out.println("Enter current pin: ");
					String currentPin = scanner.nextLine();
					if (currentPin.equals(security_pin)) {
						System.out.println("Enter new security pin (minimum 4 characters): ");
						String newPin = scanner.nextLine();
						System.out.println("Confirm new security pin (minimum 4 characters): ");
						String newPinConfirm = scanner.nextLine();
						
						if (!newPin.isEmpty() && newPin.equals(newPinConfirm)) {
							String changePinQuery = "UPDATE Account SET security_pin = ? WHERE account_number = ?";
							PreparedStatement preparedStatementChangePin = connection.prepareStatement(changePinQuery);
							preparedStatementChangePin.setString(1, newPinConfirm);
							preparedStatementChangePin.setLong(2, account_number);
							int affectedRows = preparedStatementChangePin.executeUpdate();
							if (affectedRows > 0) {
								System.out.println("Security Pin Changed Successfully!");
								System.out.println("------------------------------");
								connection.commit();
								connection.setAutoCommit(true);
							}else {
								System.out.println("Security Pin Change Failed!");
								System.out.println("------------------------------");
								connection.rollback();
								connection.setAutoCommit(true);
							}
						}else {
							System.out.println("Security Pins do not match.");
							System.out.println("------------------------------");
						}
					}else {
						System.out.println("Incorrect pin entered!");
						System.out.println("------------------------------");
					}
				}else {
					System.out.println("We understand you do not want to change pin at the moment. Thank you!");
					System.out.println("------------------------------");
				}
			}else {
				System.out.println("Invalid Pin!");
				System.out.println("------------------------------");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
