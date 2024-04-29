package com.np.schoolpgi.dto.request;

import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;

@Data
@Validated
@ToString
public class ResetPasswordRequest {
	
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String resetPassword;
	
	@NotEmpty
	private String confirmPassword;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getResetPassword() {
		return resetPassword;
	}
	public void setResetPassword(String resetPassword) {
		this.resetPassword = resetPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	

}
