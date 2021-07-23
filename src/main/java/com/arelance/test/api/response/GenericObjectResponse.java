package com.arelance.test.api.response;

public class GenericObjectResponse {
	
	private String msg;
	private Object info;
	
	public GenericObjectResponse(String msg, Object info) {
		this.msg = msg;
		this.info = info;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
}
