package com.np.schoolpgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {

	private Boolean status;
	private String message;
	private Object users;
}

