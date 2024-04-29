package com.np.schoolpgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
	private Boolean success;
	private String messageCode;
	private String message;
}
