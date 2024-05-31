package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;
//import static java.lang.Class.forName;

public class BankingApp {
	private static final String url = "jdbc:mysql://localhost:3306/<database_name>";
	private static final String username = "<username>";
	private static final String password = "<password>";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		try {
			//System.out.println("hello");
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Scanner scanner = new Scanner(System.in);
			User user = new User(connection, scanner);
			Accounts accounts = new Accounts(connection, scanner);
			AccountManager accountManager = new AccountManager(connection, scanner);
			Admin admin = new Admin(connection, scanner);
			
			String email;
			long account_number;
			
			while(true) {
				System.out.println("*** WELCOME TO SAFE BANK ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println();
                System.out.println("For ADMIN Login Press 4!");
                System.out.println();
                System.out.print("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1) {
                case 1:
                	user.register();
                	break;
                case 2:
                	email = user.login();
                	if (email != null) {
                		System.out.println();
                		System.out.println("User Logged In!");
                		System.out.println("------------------------------");
                		if (!accounts.account_exist(email)) {
                			 System.out.println();
                             System.out.println("1. Open a new Bank Account");
                             System.out.println("2. Exit");
                             
                             if (scanner.nextInt() == 1) {
                            	 account_number = accounts.open_account(email);
                            	 System.out.println("Account created successfully!");
                            	 System.out.println("Your Account Number is: "+account_number);
                            	 System.out.println("------------------------------");
                             }else {
                            	 break;
                             }
                		}
                		account_number = accounts.getAccount_number(email);
                		int choice2 = 0;
                		while(choice2 != 6) {
                			 System.out.println();
                			 System.out.println("==== WELCOME USER! ACCOUNT NUMBER: "+ account_number + " ====");
                			 System.out.println();
                             System.out.println("1. Debit Money");
                             System.out.println("2. Credit Money");
                             System.out.println("3. Transfer Money");
                             System.out.println("4. Check Balance");
                             System.out.println("5. Account Info");
                             System.out.println("6. Log Out");
                             System.out.print("Enter your choice: ");
                             choice2 = scanner.nextInt();
                             
                             switch(choice2) {
                             case 1:
                            	 accountManager.debit_money(account_number);
                            	 break;
                             case 2:
                            	 accountManager.credit_money(account_number);
                            	 break;
                             case 3:
                            	 accountManager.transfer_money(account_number);
                            	 break;
                             case 4:
                            	 accountManager.getBalance(account_number);
                            	 break;
                             case 5:
                            	 accountManager.getAccountInfo(account_number);
                            	 break;
                             case 6:
                            	 break;
                            default:
                            	System.out.println("Enter valid choice!");
                            	System.out.println("------------------------------");
                            	break;
                             }
                		}
                	}else {
                		System.out.println("Incorrect Email or Password!");
                		System.out.println("------------------------------");
                	}
                case 3:
                	System.out.println("Thank you!");
                	System.out.println("Exiting System!");
                	System.out.println("------------------------------");
                	return;
                case 4:
                	boolean adminLoggedIn = admin.adminLogin();
        			if (adminLoggedIn) {
        				int choice3 = 0;
        				while (choice3 != 3) {
        					System.out.println();
            				System.out.println("1. Get Users List");
            				System.out.println("2. Get Accounts List");
            				System.out.println("3. Logout");
            				System.out.print("Enter choice: ");
            				choice3 = scanner.nextInt();
            				switch (choice3) {
            				case 1:
            					admin.getUsersList();
            					break;
            				case 2:
            					admin.getAccountsList();
            					break;
            				case 3:
            					break;
            				default:
            					System.out.println("Enter valid choice!");
                            	System.out.println("------------------------------");
                            	break;
            				}
        				}
        			}
                default:
                	System.out.println("Enter valid choice");
                	System.out.println("------------------------------");
                	break;
                }
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
