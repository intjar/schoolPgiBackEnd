package com.np.schoolpgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	private Boolean success;
	private String errorCode;
	private String errorMessage;
	
}
