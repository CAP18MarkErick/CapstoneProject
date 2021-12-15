package com.gbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ACCOUNT_DETAILS")
public class Account {

	@Id
	@Column(name = "ACCOUNT_NUMBER", nullable = false)
	private String accountNumber;
	@Column(name = "USER_NAME")
	private String userName;
	@Column(name = "ACCOUNT_BALANCE")
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
