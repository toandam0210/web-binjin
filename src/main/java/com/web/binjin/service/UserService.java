package com.web.binjin.service;

import com.web.binjin.model.dto.UserDTO;
import com.web.binjin.model.entity.User;
import com.web.binjin.model.payload.JwtResponse;
import com.web.binjin.model.payload.LoginRequest;
import com.web.binjin.model.payload.ResponseData;
import com.web.binjin.model.payload.SignupRequest;

public interface UserService {
	 User getUserById(int id);
	 JwtResponse login(LoginRequest loginRequest) throws Exception;
	 ResponseData<User> signup(SignupRequest signUpRequest);
	 void updateUser(UserDTO user);
}
