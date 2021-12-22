package com.gbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.gbs.entity.User;
import com.gbs.entity.UserResponse;
import com.gbs.repository.UserRepository;



@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	
	@GetMapping("/get")
	public List<User> getAllUserss() {
		List<User> usersList = userRepository.findAll();
		return usersList;
	}
	
	@PutMapping("/update/{id}")
	public User updateUsers(@PathVariable(value = "id") String username, @RequestBody User accountFromBrowser) {
		User existingUsers = userRepository.findById(username).get();
		existingUsers.setTotalBalance(accountFromBrowser.getTotalBalance());
		existingUsers.setListOfAccounts(accountFromBrowser.getListOfAccounts());
		User updatedUsers = userRepository.save(existingUsers);
		return updatedUsers;
		
	}
	
	@GetMapping("/getUsers")
	public UserResponse getResponse() {
		List<User> usersList = userRepository.findAll();
		UserResponse userResponse = new UserResponse();
		userResponse.setUsers(usersList);
		return userResponse;
	}
	


		

}
