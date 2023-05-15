package com.web.binjin.model.payload;

import java.util.Set;

import javax.validation.constraints.NotBlank;

public class SignupRequest {
	@NotBlank
	private String username;

	private Set<String> roles;

	@NotBlank
	private String password;
	
	@NotBlank
	private String email;

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}