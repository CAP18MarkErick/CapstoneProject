package com.gbs;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.client.RestTemplate;

import com.gbs.entity.Account;
import com.gbs.entity.User;
import com.gbs.entity.UserResponse;

@SpringBootApplication
@EnableEurekaClient
public class GBSUserServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(GBSUserServiceApp.class, args);
	}

	public static void loginUser(String username, String password) {

		RestTemplate userRestTemplate = new RestTemplate();
		final String URLString = "http://localhost:8081/ecz/users/getUsers";
		UserResponse userResponse = userRestTemplate.getForObject(URLString, UserResponse.class);

		for (User u : userResponse.getUsers()) {

			String userNameFoundString = u.getUserName().toString();

			if (userNameFoundString.equals(username)) {
					
				String passwordFoundString = u.getUserPassword().toString();
				
				if (passwordFoundString.equals(password)) {

					System.out.println("Login success!\n\n");
					System.out.println("Welcome " + u.getUserName());
					
					
					System.out.println("SELECT OPTION\n\n[1]Account Details\n[2]Transactions\n[3]Transfer Funds\n[4]Exit");
					System.out.print("\nChoice: ");
					String choiceString = new Scanner(System.in).next();
					int choice = Integer.parseInt(choiceString);
					
					switch (choice) {
					case 1:

						GBSAccountServiceApp.getAccountDetails(username);
						
					break;
						
					case 2:
						GBSAccountServiceApp.getTansactions(username);
						
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

						GBSAccountServiceApp.transferFunds(username,faccNumber, taccNumber, amt);
						
						String receiverUsername = GBSAccountServiceApp.getReceiverUsername(taccNumber).toString();
						String receiverBalance = GBSAccountServiceApp.updateReceiverBalance(receiverUsername).toString();
						String receiverAccNum = GBSAccountServiceApp.updateReceiverListofAccount(receiverUsername).toString();
						String senderBalance = GBSAccountServiceApp.updateSenderBalance(username).toString();
						String senderAccNum = GBSAccountServiceApp.updateSenderListofAccount(username).toString();
						
						/*
						 * System.out.println(receiverBalance); System.out.println(receiverAccNum);
						 * System.out.println(senderBalance); System.out.println(senderAccNum);
						 * System.out.println(receiverUsername);
						 */
						updateUser(username, senderAccNum, senderBalance);
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
	
	public static void updateUser(String username, String listofAcc, String newBal) {
		RestTemplate updateSenderTemplate = new RestTemplate();
		double bal = Double.parseDouble(newBal);
		User userUpdate = new User();
		userUpdate.setTotalBalance(bal);
		userUpdate.setListOfAccounts(listofAcc);
		final String updateURLString = "http://localhost:8081/ecz/users/update/" + username;
		updateSenderTemplate.put(updateURLString, userUpdate);
	}
	
}

