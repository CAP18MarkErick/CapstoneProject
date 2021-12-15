package com.gbs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.client.RestTemplate;

import com.gbs.entity.Account;
import com.gbs.entity.AccountResponse;
import com.gbs.entity.Transaction;
import com.gbs.entity.TransactionResponse;

import net.bytebuddy.asm.Advice.Return;

@SpringBootApplication
@EnableEurekaClient
public class GBSAccountServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(GBSAccountServiceApp.class, args);
	}

	public static void transferFunds(String inputUserName, String fAccNumber, String tAccNumber, double sendAmt) {
		RestTemplate updateRestTemplate = new RestTemplate();
		final String URLStringGetAccount = "http://localhost:9091/ecz/account/get";
		AccountResponse accountResponse = updateRestTemplate.getForObject(URLStringGetAccount, AccountResponse.class);

		boolean fromAccountSuccess = false;
		String sender = null;
		for (Account account : accountResponse.getAccounts()) {
			if (account.getUserName().equals(inputUserName)) {
				if (account.getAccountNumber().equals(fAccNumber)) {
					double currBal = Double.parseDouble(account.getAccountBalance());
					double newBal = currBal - sendAmt;
					String newBalance = String.valueOf(newBal);
					sender = account.getUserName().toString();
						if (currBal >= sendAmt) {
							updateSender(fAccNumber, newBalance);
							fromAccountSuccess = true;
						} else {
							System.out.println("\n\nTransfer Failed. Insufficient Balance!");
							System.exit(0);
								}
						}
				else {
					System.out.println("Transfer Failed! Please check your Account number.");
					System.exit(0);
				}
			}
		}

		if (fromAccountSuccess) {
			for (Account account : accountResponse.getAccounts()) {
				if (account.getAccountNumber().equals(tAccNumber)) {
					double currBal = Double.parseDouble(account.getAccountBalance());
					double newBal = currBal + sendAmt;
					String newBalance = String.valueOf(newBal);
					String receiver = account.getUserName().toString();
					updateReceiver(tAccNumber, newBalance);

					System.out.println("\n\nYou've Successfully sent the amount of " + sendAmt + " to Account number "
							+ tAccNumber);

					createTransaction(fAccNumber, tAccNumber, String.valueOf(sendAmt));
					getReceiverUsername(receiver);

				}
			}
		}

	}

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

	public static void updateSender(String fAccNumber, String newBal) {
		RestTemplate updateSenderTemplate = new RestTemplate();
		Account accountUpdate = new Account();
		accountUpdate.setAccountBalance(newBal);
		final String updateURLString = "http://localhost:9091/ecz/account/update/" + fAccNumber;
		updateSenderTemplate.put(updateURLString, accountUpdate);
	}

	public static void updateReceiver(String tAccNumber, String newBal) {
		RestTemplate updateReceiverTemplate = new RestTemplate();
		Account accountUpdate = new Account();
		accountUpdate.setAccountBalance(newBal);
		final String updateURLString = "http://localhost:9091/ecz/account/update/" + tAccNumber;
		updateReceiverTemplate.put(updateURLString, accountUpdate);
	}

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
