package com.arelance.test.api.request;

import javax.validation.constraints.NotBlank;

public class RoleRequest {
	@NotBlank
	private String roleName;

	public RoleRequest() {
	}

	public RoleRequest(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
