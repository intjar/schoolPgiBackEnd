package com.np.schoolpgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseValue {
	private Boolean success;
	private String message;
	private Object result;
}
