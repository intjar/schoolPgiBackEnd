package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@Validated
@ToString
public class ForgetPasswordRequest {

	@NotEmpty
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
