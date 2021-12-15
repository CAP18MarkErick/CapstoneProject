package com.gbs.models;




public class Account {

	private String accountNumber;

	private String userName;

	private String accountBalance;

	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(String accountNumber, String userName, String accountBalance) {
		super();
		this.accountNumber = accountNumber;
		this.userName = userName;
		this.accountBalance = accountBalance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getUserName() {
		return userName;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

}
