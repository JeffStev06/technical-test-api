package com.arelance.test.api.request;

import javax.validation.constraints.NotBlank;

public class DepartmentRequest {
	@NotBlank
	private String name;
	private String description;
	
	public DepartmentRequest() {
	}

	public DepartmentRequest(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
