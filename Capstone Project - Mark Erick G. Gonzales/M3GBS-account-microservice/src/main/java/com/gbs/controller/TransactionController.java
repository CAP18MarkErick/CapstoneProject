package com.gbs.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.gbs.entity.Transaction;
import com.gbs.entity.TransactionResponse;
import com.gbs.repository.TransactionRepository;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@GetMapping("/get")
	public TransactionResponse getAllTransaction() {
		List<Transaction> usersList = transactionRepository.findAll();
		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setTransactions(usersList);
		
		return transactionResponse;
	}
	
	
	@PostMapping("/create")
	public Transaction createTransaction(@RequestBody Transaction transactionFromBrowser) {
		
		Transaction saveTransaction = transactionRepository.save(transactionFromBrowser);
		
		return saveTransaction;
		
	}
	
	

}
