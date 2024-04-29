package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@Validated
@ToString
public class UpdatePasswordRequest {

	@NotNull
	private int loggedInUserId;
	
	@NotBlank
	private String currentpassword;
	
	@NotBlank
	private String newPassword;
	
	@NotBlank
	private String confirmPassword;
	
	
}



