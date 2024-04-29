package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {
	@NotBlank(message = "Must not be null")
//	@Size(min = 6, max = 256, message = "Username must be between 6 and 256")
//	@Pattern(regexp = Regex.USER_NAME)
	private String username;
	
	@NotBlank(message = "Must not be null")
//	@Size(min = 8, max = 20, message = "Password must be between 6 and 256")
//	@Pattern(regexp = Regex.PASSWORD_VALIDATION, message = "Invalid Password")
    private String password;
}
