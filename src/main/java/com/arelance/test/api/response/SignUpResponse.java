package com.arelance.test.api.response;

public class SignUpResponse {
	
	private String email;
	private String temporal;
	
	public SignUpResponse(String email, String temporal) {
		this.email = email;
		this.temporal = temporal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTemporal() {
		return temporal;
	}

	public void setTemporal(String temporal) {
		this.temporal = temporal;
	}
	
}
