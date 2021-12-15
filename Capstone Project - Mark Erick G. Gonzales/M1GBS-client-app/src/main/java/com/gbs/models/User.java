package com.gbs.models;

import java.time.LocalDateTime;




public class User {


		private String userName;
	
		private String userPassword;
	
		LocalDateTime createdAt = LocalDateTime.now();
	
		private int numberOfAccounts;

		private String listOfAccounts;
	
		private double totalBalance;
	
		private String contactNumber;
		
		public User() {
			super();
		}

		public User(String userName, String userPassword, LocalDateTime createdAt, int numberOfAccounts,
				String listOfAccounts, double totalBalance, String contactNumber) {
			super();
			this.userName = userName;
			this.userPassword = userPassword;
			this.createdAt = createdAt;
			this.numberOfAccounts = numberOfAccounts;
			this.listOfAccounts = listOfAccounts;
			this.totalBalance = totalBalance;
			this.contactNumber = contactNumber;
		}

		public String getUserName() {
			return userName;
		}

		public String getUserPassword() {
			return userPassword;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public int getNumberOfAccounts() {
			return numberOfAccounts;
		}

		public String getListOfAccounts() {
			return listOfAccounts;
		}

		public double getTotalBalance() {
			return totalBalance;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public void setUserPassword(String userPassword) {
			this.userPassword = userPassword;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public void setNumberOfAccounts(int numberOfAccounts) {
			this.numberOfAccounts = numberOfAccounts;
		}

		public void setListOfAccounts(String listOfAccounts) {
			this.listOfAccounts = listOfAccounts;
		}

		public void setTotalBalance(double totalBalance) {
			this.totalBalance = totalBalance;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		
		
		
		
		
		
}
