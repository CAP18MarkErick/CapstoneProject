package com.gbs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import org.springframework.web.client.RestTemplate;

import com.gbs.entity.User;
import com.gbs.entity.UserResponse;
import com.gbs.models.Account;
import com.gbs.models.Transaction;
import com.gbs.stub.AccountResponse;

import com.gbs.stub.TransactionResponse;




public class GBSClientApp {

	public static void main(String[] args) {
		
		
		System.out.println("*******************************************");
		System.out.println("GLOBAL BANKING SYSTEM");
		System.out.println("*******************************************");

		Scanner scanner = new Scanner(System.in);

		System.out.println("Select Option\n[1]Login\n[2]Exit");
		System.out.print("Choice: ");

		String choiceString = scanner.next();

		int choice = Integer.parseInt(choiceString);

		if (choice == 1) {
			System.out.print("\nUsername : ");
			String username = new Scanner(System.in).nextLine();
			System.out.print("Password : ");
			String password = new Scanner(System.in).nextLine();
			// CALLING THIS METHOD TO VALIDATE THE LOGIN
			loginUser(username, password);

		}

		else if (choice == 2) {
			System.out.println("Thank you!");
			System.exit(1);
		}

		else {
			System.out.println("Invalid Input!");
		}

	}
	// LOGIN VALIDATION
	public static void loginUser(String username, String password) {

		RestTemplate userRestTemplate = new RestTemplate();
		final String URLString = "http://localhost:8081/ecz/users/getUsers";
		UserResponse userResponse = userRestTemplate.getForObject(URLString, UserResponse.class);

		for (User u : userResponse.getUsers()) {

			String userNameFoundString = u.getUserName().toString();

			if (userNameFoundString.equals(username)) {
					
				String passwordFoundString = u.getUserPassword().toString();
				
				if (passwordFoundString.equals(password)) {
					System.out.println("\n****************************************************");
					System.out.println("Login success!\n\n");
					System.out.println("Welcome " + u.getUserName());
					
					
					System.out.println("SELECT OPTION\n\n[1]Account Details\n[2]Transactions\n[3]Transfer Funds\n[4]Exit");
					System.out.print("\nChoice: ");
					String choiceString = new Scanner(System.in).next();
					int choice = Integer.parseInt(choiceString);
					
					switch (choice) {
					case 1:
						// CALLS THE METHOD THAT RETRIEVE THE ACCOUNT DETAILS OF THE SPECIFIC USER
						getAccountDetails(username);
						
					break;
						
					case 2:
						// CALLS THE METHOD THAT RETRIEVE THE TRANSACTION HISTORY
						getTansactions(username);
						
					break;
						
					case 3:
						System.out.println("\n\nTRANSFER MONEY\n\n");
						System.out.println("FROM ACCOUNT NUMBER: ");
						String faccNumber = new Scanner(System.in).nextLine();
						System.out.println("TO ACCOUNT NUMBER: ");
						String taccNumber = new Scanner(System.in).nextLine();
						System.out.println("AMOUNT: ");
						String amount = new Scanner(System.in).nextLine();
						double amt = Double.parseDouble(amount);
						// CALS THE METHOD FOR TRANSFERING FUNDS
						transferFunds(username,faccNumber, taccNumber, amt);
						
						// PASSING STRING VALUES FROM METHODS
						String receiverUsername = getReceiverUsername(taccNumber).toString();
						String receiverBalance = updateReceiverBalance(receiverUsername).toString();
						String receiverAccNum = updateReceiverListofAccount(receiverUsername).toString();
						String senderBalance = updateSenderBalance(username).toString();
						String senderAccNum = updateSenderListofAccount(username).toString();
						// UPDATE USER TABLE FOR SENDER
						updateUser(username, senderAccNum, senderBalance);
						// UPDATE USER TABLE FOR RECEIVER
						updateUser(receiverUsername, receiverAccNum, receiverBalance);
					break;

					default:
						
						System.out.println("Thank you!");
						System.exit(0);
						break;
						
						
					}
				

				} else {
					System.out.println("Incorrect username or password. ");
					
				}

			}
		}
		
	}
	
	// UPDATE FOR USER TABLE
	public static void updateUser(String username, String listofAcc, String newBal) {
		RestTemplate updateSenderTemplate = new RestTemplate();
		double bal = Double.parseDouble(newBal);
		User userUpdate = new User();
		userUpdate.setTotalBalance(bal);
		userUpdate.setListOfAccounts(listofAcc);
		final String updateURLString = "http://localhost:8081/ecz/users/update/" + username;
		updateSenderTemplate.put(updateURLString, userUpdate);
	}

	
	
	// TRANSFERING FUNDS METHOD
	public static void transferFunds(String inputUserName, String fAccNumber, String tAccNumber, double sendAmt) {
		RestTemplate updateRestTemplate = new RestTemplate();
		final String URLStringGetAccount = "http://localhost:9091/ecz/account/get";
		AccountResponse accountResponse = updateRestTemplate.getForObject(URLStringGetAccount, AccountResponse.class);

		boolean fromAccountSuccess = false;
		boolean found = false;
		String sender = null;
		for (Account account : accountResponse.getAccounts()) {
			if (account.getUserName().equals(inputUserName)) {
				String accnumString = account.getAccountNumber();
				if (accnumString.equals(fAccNumber)) {
					double currBal = Double.parseDouble(account.getAccountBalance());
					double newBal = currBal - sendAmt;
					String newBalance = String.valueOf(newBal);
					sender = account.getUserName().toString();
						if (currBal >= sendAmt) {
							// CALLS THIS METHOD WHEN THE TRANSFER MEETS THE CONDITION
							updateSender(fAccNumber, newBalance);
							fromAccountSuccess = true;
							found = true;
						} else {
							System.out.println("\n\nTransfer Failed. Insufficient Balance!");
							System.exit(0);
								}
						}
			}
			  
		}
		if (!found) {
			  System.out.println("Transfer Failed! Please check your Account number.");
			  System.exit(0); 
		}

		if (fromAccountSuccess) {
			for (Account account : accountResponse.getAccounts()) {
				if (account.getAccountNumber().equals(tAccNumber)) {
					double currBal = Double.parseDouble(account.getAccountBalance());
					double newBal = currBal + sendAmt;
					String newBalance = String.valueOf(newBal);
					String receiver = account.getUserName().toString();
					// CALLS THIS METHOD WHEN THE TRANSFER MEETS THE CONDITION
					updateReceiver(tAccNumber, newBalance);

					System.out.println("\n\nYou've Successfully sent the amount of " + sendAmt + " to Account number "
							+ tAccNumber);
					// CALLS THIS METHOD WHEN THE TRANSFER IS COMPLETE AND IT WILL GENERATE A TRANSACTION IN TRANSACTION TABLE
					createTransaction(fAccNumber, tAccNumber, String.valueOf(sendAmt));
					// THIS METHOD IS FOR FETCHING THE USERNAME OF THE RECEIVER. THIS IS TO BE USED IN UPDATE USERS TABLE.
					getReceiverUsername(receiver);

				}
			}
		}

	}
	
	// METHOD THAT RETURNS USERNAME OF RECEIVER AS A STRING. 
	//THIS WILL BE USED AS A REFERENCE FOR UPDATING USERS TABLE OF THE SENDER.
	public static String getReceiverUsername(String toAccount) {
		RestTemplate updateRestTemplate = new RestTemplate();
		String receiver = null;
		final String URLStringGetAccount = "http://localhost:9091/ecz/account/get";
		AccountResponse accountResponse = updateRestTemplate.getForObject(URLStringGetAccount, AccountResponse.class);
		for (Account account : accountResponse.getAccounts()) {
			if (account.getAccountNumber().equals(toAccount)) {

				receiver = account.getUserName().toString();

			}
		}

		return receiver;
	}
	// METHOD THAT RETURNS SENDER BALANCE AS A STRING.
	// THIS WILL BE USED AS A REFERENCE FOR UPDATING USERS TABLE OF THE SENDER.
	public static String updateSenderBalance(String username) {
		RestTemplate accountRestTemplate = new RestTemplate();
		final String urlRESTAPISelect = "http://localhost:9091/ecz/account/get";

		AccountResponse accforUserUpdate = accountRestTemplate.getForObject(urlRESTAPISelect, AccountResponse.class);
		double senderbalDouble = 0.0;
		for (Account accounts : accforUserUpdate.getAccounts()) {

			if (accounts.getUserName().equals(username)) {
				senderbalDouble += Double.parseDouble(accounts.getAccountBalance());
			}

		}

		return String.valueOf(senderbalDouble);
	}
	
	
	// METHOD THAT RETURNS SENDER LIST OF ACCOUNT AS A STRING.
	// THIS WILL BE USED AS A REFERENCE FOR UPDATING USERS TABLE OF THE SENDER.
	public static String updateSenderListofAccount(String username) {
		RestTemplate accountRestTemplate = new RestTemplate();
		final String urlRESTAPISelect = "http://localhost:9091/ecz/account/get";
		AccountResponse accforUserUpdate = accountRestTemplate.getForObject(urlRESTAPISelect, AccountResponse.class);
		final StringBuilder senderbuilder = new StringBuilder();
		String delimiter = "";
		for (Account accounts : accforUserUpdate.getAccounts()) {

			if (accounts.getUserName().equals(username)) {
				senderbuilder.append(delimiter);
				delimiter = ", ";
				senderbuilder.append(accounts.getAccountNumber());

			}

		}
		String senderconcatString = senderbuilder.toString();
		return senderconcatString;
	}
	
	// METHOD THAT RETURNS RECEIVER BALANCE AS A STRING.
	// THIS WILL BE USED AS A REFERENCE FOR UPDATING USERS TABLE OF THE RECEIVER.
	public static String updateReceiverBalance(String username) {
		RestTemplate accountRestTemplate = new RestTemplate();
		final String urlRESTAPISelect = "http://localhost:9091/ecz/account/get";

		AccountResponse accforUserUpdate = accountRestTemplate.getForObject(urlRESTAPISelect, AccountResponse.class);

		double receiverbalDouble = 0.0;

		for (Account accounts : accforUserUpdate.getAccounts()) {
			if (accounts.getUserName().equals(username)) {
				receiverbalDouble += Double.parseDouble(accounts.getAccountBalance());
			}

		}

		return String.valueOf(receiverbalDouble);

	}
	// METHOD THAT RETURNS RECEIVER LIST OF ACCOUNT AS A STRING.
	// THIS WILL BE USED AS A REFERENCE FOR UPDATING USERS TABLE OF THE RECEIVER.
	public static String updateReceiverListofAccount(String username) {
		RestTemplate accountRestTemplate = new RestTemplate();
		final String urlRESTAPISelect = "http://localhost:9091/ecz/account/get";

		AccountResponse accforUserUpdate = accountRestTemplate.getForObject(urlRESTAPISelect, AccountResponse.class);

		final StringBuilder receiverbuilder = new StringBuilder();
		String delimiter = "";
		for (Account accounts : accforUserUpdate.getAccounts()) {

			if (accounts.getUserName().equals(username)) {

				receiverbuilder.append(delimiter);
				delimiter = ", ";
				receiverbuilder.append(accounts.getAccountNumber());

			}
		}
		String receiverconcatString = receiverbuilder.toString();

		return receiverconcatString;
	}
	// THIS METHOD IS RESPONSIBLE FOR UPDATING THE BALANCE OF THE SENDER. THIS WILL REFLECT ON THE ACCOUNT_DETAILS TABLE.
	public static void updateSender(String fAccNumber, String newBal) {
		RestTemplate updateSenderTemplate = new RestTemplate();
		Account accountUpdate = new Account();
		accountUpdate.setAccountBalance(newBal);
		final String updateURLString = "http://localhost:9091/ecz/account/update/" + fAccNumber;
		updateSenderTemplate.put(updateURLString, accountUpdate);
	}
	// THIS METHOD IS RESPONSIBLE FOR UPDATING THE BALANCE OF THE RECEIVER. THIS WILL REFLECT ON THE ACCOUNT_DETAILS TABLE.
	public static void updateReceiver(String tAccNumber, String newBal) {
		RestTemplate updateReceiverTemplate = new RestTemplate();
		Account accountUpdate = new Account();
		accountUpdate.setAccountBalance(newBal);
		final String updateURLString = "http://localhost:9091/ecz/account/update/" + tAccNumber;
		updateReceiverTemplate.put(updateURLString, accountUpdate);
	}
	// THIS METHOD IS RESPONSIBLE FOR FETCHING THE ACCOUNT DETAILS OF THE USER IN THE REQUIRED PARAMETER.
	public static void getAccountDetails(String username) {

		int count = 0;

		RestTemplate accountRestTemplate = new RestTemplate();
		final String urlRESTAPISelect = "http://localhost:9091/ecz/account/get";

		AccountResponse accountResponse = accountRestTemplate.getForObject(urlRESTAPISelect, AccountResponse.class);

		for (Account account : accountResponse.getAccounts()) {

			if (account.getUserName().equals(username)) {
				count++;
			}
		}

		System.out.println("****************************************************");
		System.out.println("YOU HAVE " + count + " ACTIVE ACCOUNT/S");
		System.out.println();

		for (Account account : accountResponse.getAccounts()) {

			if (account.getUserName().equals(username)) {
				System.out.println("ACCOUNT NUMBER: " + account.getAccountNumber());
				System.out.println("ACCOUNT BALANCE: " + account.getAccountBalance());

				System.out.println();

			}
		}
		System.out.println("****************************************************");

	}
	// THIS METHOD IS RESPONSIBLE FOR FETCHING ALL THE TRANSACTIONS OF THE USER IN THE REQUIRED PARAMETER.
	public static void getTansactions(String username) {
		boolean toFound = false;
		boolean found = false;
		int count = 0;
		RestTemplate getRestTemplate = new RestTemplate();
		final String URLStringGetTransaction = "http://localhost:9091/ecz/transaction/get";
		TransactionResponse transactionResponse = getRestTemplate.getForObject(URLStringGetTransaction,
				TransactionResponse.class);

		

		RestTemplate getAccountRestTemplate = new RestTemplate();
		final String URLStringGetAccount = "http://localhost:9091/ecz/account/get";
		AccountResponse accountResponse = getAccountRestTemplate.getForObject(URLStringGetAccount,
				AccountResponse.class);

		for (Account account : accountResponse.getAccounts()) {

			if (account.getUserName().equals(username)) {
				count++;
			}
		}
		
		System.out.println("\n\nTRANSACTION HISTORY\n\n");
		System.out.println("****************************************************");
		System.out.println("YOU HAVE " + count + " ACTIVE ACCOUNT/S\n");

		for (Account account : accountResponse.getAccounts()) {

			if (account.getUserName().equals(username)) {
				System.out.println("ACCOUNT NUMBER: " + account.getAccountNumber());
				System.out.println();
			}
		}
		System.out.println("****************************************************");
		System.out.println();
		System.out.print("ENTER ACCOUNT NUMBER: ");
		String inputAccountNumber = new Scanner(System.in).next();

		for (Account account : accountResponse.getAccounts()) {

			if (account.getUserName().equals(username)) {
				if (account.getAccountNumber().equals(inputAccountNumber)) {

					toFound = true;

				}
			}
		}

		if (!toFound) {
			System.out.println(inputAccountNumber + " ACCOUNT NUMBER DOES NOT EXIST");

		} else {
			System.out.println("\n\nTRANSACTION HISTORY OF ACCOUNT NUMBER : " + inputAccountNumber);
			for (Transaction transaction : transactionResponse.getTransactions()) {

				if (transaction.getFromAccount().equals(inputAccountNumber)) {
					found = true;
				} else if (transaction.getToAccount().equals(inputAccountNumber)) {
					found = true;
				}
			}

			if (found) {
				for (Transaction transaction : transactionResponse.getTransactions()) {

					if (transaction.getFromAccount().equals(inputAccountNumber)) {
						System.out.println("****************************************************");
						System.out.println("DATE AND TIME SENT : " + transaction.getTransactionDate());
						System.out.println("TRANSACTION ID: " + transaction.getTransactionId() + "\nTO ACCOUNT: "
								+ transaction.getToAccount() + "\nAMOUNT: " + transaction.getAmount());

					} else if (transaction.getToAccount().equals(inputAccountNumber)) {
						System.out.println("****************************************************");
						System.out.println("DATE AND TIME RECEIVED : " + transaction.getTransactionDate());
						System.out.println("TRANSACTION ID: " + transaction.getTransactionId() + "\nFROM ACCOUNT: "
								+ transaction.getFromAccount() + "\nAMOUNT: " + transaction.getAmount());

					}
					
				}
				System.out.println("****************************************************");

			} else {
				System.out.println("****************************************************");
				System.out.println("YOU DON'T HAVE ANY TRANSACTION AT THE MOMENT " + username);
				System.out.println("****************************************************");
			}

		}
	}
	// THIS METHOD IS RESPONSIBLE FOR RECORDINGTHE TRANSACTIONS
	public static void createTransaction(String fromAccNumber, String toAccNumber, String transactAmount) {

		Random rnd = new Random();
		int length = 9;
		char fill = 0;
		int number = rnd.nextInt(999999999);

		String rdnString = new String(new char[length - String.valueOf(number).length()]).replace('\0', fill) + number;
		// Date
		String dateNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());

		boolean exist = false;

		RestTemplate transactionRestTemplate = new RestTemplate();
		final String urlRESTAPISelect = "http://localhost:9091//ecz/transaction/get";
		TransactionResponse transactionResponse = transactionRestTemplate.getForObject(urlRESTAPISelect,
				TransactionResponse.class);

		for (Transaction transaction : transactionResponse.getTransactions()) {
			if (transaction.getTransactionId().equals(rdnString)) {
				exist = true;
			}
		}

		if (!exist) {

			final String urlRESTAPICreate = "http://localhost:9091//ecz/transaction/create";
			Transaction transaction = new Transaction();
			transaction.setTransactionId(rdnString);
			transaction.setFromAccount(fromAccNumber);
			transaction.setToAccount(toAccNumber);
			transaction.setAmount(transactAmount);
			transaction.setTransactionDate(dateNow);
			Transaction createTransactions = transactionRestTemplate.postForObject(urlRESTAPICreate, transaction,
					Transaction.class);
		} else {
			int rdnStringInt = Integer.parseInt(rdnString);
			int numberRdn = rnd.nextInt(9999);
			int newIdInt = rdnStringInt - numberRdn;
			String newIdString = String.valueOf(newIdInt);
			final String urlRESTAPICreate = "http://localhost:9091//ecz/transaction/create";
			Transaction transaction = new Transaction();
			transaction.setTransactionId(newIdString);
			transaction.setFromAccount(fromAccNumber);
			transaction.setToAccount(toAccNumber);
			transaction.setAmount(transactAmount);
			transaction.setTransactionDate(dateNow);

			Transaction createTransactions = transactionRestTemplate.postForObject(urlRESTAPICreate, transaction,
					Transaction.class);

		}

	}
	
	

}
