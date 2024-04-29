package com.np.schoolpgi.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiResponse {
	private HttpStatus status;
	private Error error;
    private Object result;
    
	public ApiResponse() {
		super();
	}

	public ApiResponse(HttpStatus status, Error error, Object result) {
		super();
		this.status = status;
		this.error = error;
		this.result = result;
	}
}
