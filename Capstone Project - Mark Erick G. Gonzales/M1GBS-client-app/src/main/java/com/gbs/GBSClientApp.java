package com.gbs;

import java.util.Scanner;


/*
import com.gbs.models.Account;
import com.gbs.models.User;
import com.gbs.models.Transaction;

import com.gbs.stub.AccountResponse;
import com.gbs.stub.UserResponse;
import com.gbs.stub.TransactionResponse;*/


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
			String username =  new Scanner(System.in).nextLine();
			System.out.print("Password : ");
			String password =  new Scanner(System.in).nextLine();
			
			GBSUserServiceApp.loginUser(username, password);
			
		}
		

		else if (choice == 2) {
			System.out.println("Thank you!");
			System.exit(1);
		}
		
		else {
			System.out.println("Invalid Input!");
		}
		
		
		
	}

}
