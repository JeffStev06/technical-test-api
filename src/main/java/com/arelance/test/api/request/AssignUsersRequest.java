package com.arelance.test.api.request;

import java.util.List;

public class AssignUsersRequest {

	private List<Integer> usersToAdd;
	private List<Integer> usersToRemove;

	public AssignUsersRequest() {
	}

	public AssignUsersRequest(List<Integer> usersToAdd, List<Integer> usersToRemove) {
		this.usersToAdd = usersToAdd;
		this.usersToRemove = usersToRemove;
	}

	public List<Integer> getUsersToAdd() {
		return usersToAdd;
	}

	public void setUsersToAdd(List<Integer> usersToAdd) {
		this.usersToAdd = usersToAdd;
	}

	public List<Integer> getUsersToRemove() {
		return usersToRemove;
	}

	public void setUsersToRemove(List<Integer> usersToRemove) {
		this.usersToRemove = usersToRemove;
	}
	
}
