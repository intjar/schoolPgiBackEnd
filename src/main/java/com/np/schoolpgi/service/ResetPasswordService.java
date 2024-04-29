package com.np.schoolpgi.service;

import com.np.schoolpgi.dto.request.UpdatePasswordRequest;

public interface ResetPasswordService {

	Integer resetpassword(String email,String resetPassword,String confirmPassword);
	Integer updateChangePassword(UpdatePasswordRequest updatePasswordRequest);
}
