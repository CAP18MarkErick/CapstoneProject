package com.gbs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gbs.entity.Account;
import com.gbs.entity.AccountResponse;
import com.gbs.repository.AccountRepository;





@RestController
@RequestMapping("/account")
public class AccountController {

		@Autowired
		AccountRepository accountRepository;
		
		@GetMapping("/get")
		public AccountResponse getAllUserss() {
			List<Account> usersList = accountRepository.findAll();
			AccountResponse accountResponse = new AccountResponse();
			accountResponse.setAccounts(usersList);
			return accountResponse;
		}
		
		@GetMapping("/get/{id}")
		public Optional<Account> getAccountDetails(@PathVariable(value = "id") String username) {
			Optional<Account> usersList = accountRepository.findById(username);
			return usersList;
		}
		 
		
		@PostMapping("/create")
		public Account createUsers(@RequestBody Account productFromBrowser) {
			System.out.println("Inserting : " + productFromBrowser);
			Account savedUsers = accountRepository.save(productFromBrowser);
			return savedUsers;
		}
		
		@PutMapping("/update/{id}")
		public Account updateUsers(@PathVariable(value = "id") String accnumber, @RequestBody Account accountFromBrowser) {
			Account existingUsers = accountRepository.findById(accnumber).get();
			existingUsers.setAccountBalance(accountFromBrowser.getAccountBalance());
			Account updatedUsers = accountRepository.save(existingUsers);
			return updatedUsers;
		}
	
}
