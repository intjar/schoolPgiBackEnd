package com.np.schoolpgi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelResponse {
	private Boolean success;
	private String message;
	private Object roles;
}
