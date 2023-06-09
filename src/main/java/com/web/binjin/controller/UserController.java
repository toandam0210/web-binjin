package com.web.binjin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.binjin.model.entity.User;
import com.web.binjin.model.payload.LoginRequest;
import com.web.binjin.model.payload.ResponseData;
import com.web.binjin.model.payload.SignupRequest;
import com.web.binjin.repository.UserRepository;
import com.web.binjin.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
		return ResponseEntity.ok(userService.login(loginRequest));
	}

	@PostMapping("/signup")
	public ResponseEntity<ResponseData<User>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return ResponseEntity.ok(userService.signup(signUpRequest));
	}


}
