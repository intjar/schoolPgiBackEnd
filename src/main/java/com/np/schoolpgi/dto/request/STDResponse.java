package com.np.schoolpgi.dto.request;

import lombok.Data;

@Data
public class STDResponse {
	private Boolean success;
	private String messageCode;
	private String message;
	private String errorCode;
	private String errorMessage;
	private Object result;
}
