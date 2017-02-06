package com.heroku.security.entities;

import java.util.ArrayList;
import java.util.List;

public class UserInputDto {
	private String username;
	private String password;
	private String passwordConfirm;
	private String email;
	private List<String> roles;

	public UserInputDto(String username, String password, String passwordConfirm, String email, List<String> roles) {
		super();
		this.username = username;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.email = email;
		this.roles = roles;
	}

	public UserInputDto(String username, String password, String passwordConfirm, String email, String role) {
		super();
		this.username = username;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.email = email;
		this.roles = new ArrayList<String>();
		this.roles.add(role);
	}

	public UserInputDto() {
		super();
	}

	public String getUsername() {
		return username;
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

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		String ret = "";
		
		ret += "username 		= " + this.username;
		ret += ",password 		= " + this.password;
		ret += ",passwordConfirm = " + this.passwordConfirm;
		ret += ",email 			= " + this.email;
		ret += ",roles 			= [";
		for (String r : this.roles)
				ret+= r + ",";
		ret += "]";
		
		return ret;
	}
}
