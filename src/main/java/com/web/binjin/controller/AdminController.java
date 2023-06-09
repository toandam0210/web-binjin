package com.web.binjin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.binjin.model.dto.UserDTO;
import com.web.binjin.model.entity.User;
import com.web.binjin.model.payload.ResponseData;
import com.web.binjin.repository.UserRepository;
import com.web.binjin.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@PutMapping("/updateuser")
	public ResponseEntity<ResponseData<User>> updateUser(@Valid @RequestBody UserDTO user) {
		ResponseData<User> response = new ResponseData<User>();
		try {
			userService.updateUser(user);
			response.setData(userRepository.findByUsername(user.getUsername()).get());
			response.setStatus("OK");
			response.setMessage("Update success");
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.toString());
		}
		return ResponseEntity.ok(response);
	}
}
