package com.arelance.test.api.security.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SignUpUserRequest {
	@NotBlank
    private String name;
	@NotBlank
    private String last;
	@NotBlank
    private String address;
	@NotBlank
    private String dni;
	@Email
	@NotBlank
    private String email;
	@NotBlank
    private String role;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
