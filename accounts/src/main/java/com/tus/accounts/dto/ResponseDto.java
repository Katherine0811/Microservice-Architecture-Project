package com.tus.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ResponseDto {
	
	private String statusCode;
	private String statusMessage;
	
	public ResponseDto(String statusCode, String statusMessage) {
		super();
		this.setStatusCode(statusCode);
		this.setStatusMessage(statusMessage);
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}