package com.customer_portal.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude
public class GeneralResponse {

	private String status;
	private Integer error;
	private String message;
	private Object data;

	public GeneralResponse() {
		super();
	}

	public GeneralResponse(String status, Integer error, String message, Object data) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getError() {
		return error;
	}

	public void setError(Integer error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GeneralResponse [status=" + status + ", error=" + error + ", message=" + message + ", data=" + data
				+ "]";
	}

}
