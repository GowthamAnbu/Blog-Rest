package com.gowtham.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.exception.ServiceException;
import com.gowtham.model.User;
import com.gowtham.service.UserService;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@PostMapping
	public ResponseEntity<Object> login(@RequestBody User user){
		user.setUserName(user.getUserName());
		user.setPassword(user.getPassword());
		UserService userService = new UserService();
		try {
			userService.login(user);
			user=userService.getUser(user.getUserName());
			if(user.getRole().getId()==1){
				return new ResponseEntity<Object>("this is a admin user",HttpStatus.ACCEPTED);
			}
			return new ResponseEntity<Object>("this is a normal user",HttpStatus.ACCEPTED);
		} catch (ServiceException e) {
			return new ResponseEntity<Object>("invalid user",HttpStatus.OK);
		}
	}
	
}
