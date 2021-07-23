package com.arelance.test.api.response;

import java.util.ArrayList;
import java.util.List;

public class GenericListResponse {
	
	private String msg;
	private int count;
	private List info = new ArrayList<>();
	
	public GenericListResponse(String msg, List info) {
		this.msg = msg;
		this.count = info.size();
		this.info = info;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List getInfo() {
		return info;
	}

	public void setInfo(List info) {
		this.info = info;
	}
	
}
