package com.gbs.models;


public class Transaction {

	private String transactionId;

	private String fromAccount;

	private String toAccount;

	private String amount;

	private String transactionDate;
	
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transaction(String transactionId, String fromAccount, String toAccount, String amount,
			String transactionType, String transactionDate) {
		super();
		this.transactionId = transactionId;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;

		this.transactionDate = transactionDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public String getAmount() {
		return amount;
	}



	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}


	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}



	
	
	
	
	
}
