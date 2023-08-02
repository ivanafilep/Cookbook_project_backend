package com.praksa.team4.entities.dto;

public class UserTokenDTO {
	
	private String email;
	
	private String token;

	private String role;
	
	public UserTokenDTO() {}
	
	public UserTokenDTO(String email, String token, String role) {
		super();
		this.email = email;
		this.token = token;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
