package com.tus.accounts.dto;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ErrorResponseDto {
	
	private String apiPath;
	private HttpStatus errorCode;
	private String errorMessage;
	private LocalDateTime errorTime;
	
	public ErrorResponseDto(String description, HttpStatus badRequest, String message, LocalDateTime now) {
		this.apiPath = description;
		this.errorCode = badRequest;
		this.errorMessage = message;
		this.errorTime = now;
	}
	
	public String getApiPath() {
		return apiPath;
	}
	
	public HttpStatus getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public LocalDateTime getErrorTime() {
		return errorTime;
	}
	
	
}