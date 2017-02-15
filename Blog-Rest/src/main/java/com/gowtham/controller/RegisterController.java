package com.gowtham.controller;


import org.apache.commons.mail.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gowtham.exception.ServiceException;
import com.gowtham.model.User;
import com.gowtham.service.UserService;
import com.gowtham.util.MailUtil;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@PostMapping
	public ResponseEntity<Object> saveUser(@RequestBody User user) {
		user.setName(user.getName());
		user.setUserName(user.getUserName());
		user.setPassword(user.getPassword());
		user.setEmailId(user.getEmailId());
		user.setPhoneNumber(user.getPhoneNumber());
		final UserService userService = new UserService();
		try {
			userService.register(user);
			MailUtil.sendSimpleMail(user);
			return new ResponseEntity<Object>("Registered and email sent",HttpStatus.ACCEPTED);
		} catch (ServiceException e) {
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.OK);
		} catch (EmailException e) {
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.OK);
		}
	}
}	
