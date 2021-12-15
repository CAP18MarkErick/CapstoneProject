package com.gbs.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {



		@Id
		@Column(name = "USER_NAME")
		private String userName;
		@Column(name = "PASSWORD")
		private String userPassword;
		@Column(name = "CREATION_DATE")
		LocalDateTime createdAt = LocalDateTime.now();
		@Column(name = "NUMBER_OF_ACCOUNTS")
		private int numberOfAccounts;
		@Column(name = "LIST_OF_ACCOUNTS")
		private String listOfAccounts;
		@Column(name = "TOTAL_BALANCE")
		private double totalBalance;
		@Column(name = "CONTACT_NUMBER")
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
