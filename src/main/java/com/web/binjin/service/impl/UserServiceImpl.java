package com.web.binjin.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.binjin.model.dto.UserDTO;
import com.web.binjin.model.entity.ERole;
import com.web.binjin.model.entity.Role;
import com.web.binjin.model.entity.User;
import com.web.binjin.model.payload.JwtResponse;
import com.web.binjin.model.payload.LoginRequest;
import com.web.binjin.model.payload.MessageResponse;
import com.web.binjin.model.payload.SignupRequest;
import com.web.binjin.repository.RoleRepository;
import com.web.binjin.repository.UserRepository;
import com.web.binjin.security.jwt.JwtUtils;
import com.web.binjin.security.service.UserDetailsImpl;
import com.web.binjin.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public User getUserById(int id) {
		return null;

	}

	@Override
	public JwtResponse login(LoginRequest loginRequest) throws Exception {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateTokenFromUsername(loginRequest.getUsername());
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);

	}

	@Override
	public ResponseEntity<?> signup(SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@Override
	public void updateUser(UserDTO user) {
		Optional<User> userOp = userRepository.findByUsername(user.getUsername());
		if (userOp.isPresent()) {
			User userUpdate = userOp.get();
			userUpdate.setEmail(user.getEmail());
			userUpdate.setPassword(user.getPassword());
			Set<Role> roles = new HashSet<Role>();
			user.getRoles().forEach(role -> {
				Role roleInDb = roleRepository.findByName(ERole.valueOf(role))
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(roleInDb);
			});
			userUpdate.setRoles(roles);
			userUpdate.setRoles(roles);
			userRepository.save(userUpdate);
		}
	}

}
