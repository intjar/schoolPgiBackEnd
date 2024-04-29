package com.np.schoolpgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LevelResponseFormat {
	private Boolean status;
	private String message;
	private Object result;
}
